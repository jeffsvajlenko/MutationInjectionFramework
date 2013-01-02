#!/usr/bin/env python
# -*- coding: utf-8 -*-

# reference implementation of clone data parser

# 2008/06/06 Improvement friendliness to IronPython

import struct
import sys
if sys.platform != 'cli':
	import codecs

class CloneDataError(IOError):
	pass

class CloneDataInvalidDataError(CloneDataError):
	pass

class CloneDataVersionMismatchError(CloneDataError):
	pass

class CloneDataReader(object):
	def __init__(self, clonedatafile):
		self.__file = clonedatafile
		if sys.platform == 'cli':
			# IronPython's string is UTF8.
			self.__decode = lambda s: s
		else:
			dec = codecs.getdecoder("utf-8")
			self.__decode = lambda s: dec(s)[0]
		self.__lastcalled = None

	def getversion(self):
		assert self.__lastcalled == None
		self.__lastcalled = "getversion"

		f = self.__file
		
		# check magic number
		value = f.read(8)
		if value != "ccfxraw0":
			raise CloneDataInvalidDataError, "invalid signature"
		
		# scan version
		siz = struct.calcsize("<I") * 3
		buf = f.read(siz)
		if len(buf) != siz:
			raise IOError, "i/o error in reading version number"
		vers = struct.unpack("<III", buf)
		if not(vers[0] == 10 and vers[1] in ( 0, 1, 2 )):
			raise CloneDataVersionMismatchError, "version mismatch"
		
		v1 = vers[1]
		if v1 == 0:
			self.iteroption = self.__parse_options_v10_0
			self.iterfileinfo = self.__parse_fileinfos
			self.iterfileremark = self.__parse_fileremarks_none
			self.itercloneremark = self.__parse_cloneremarks_none
		elif v1 == 1:
			self.iteroption = self.__parse_options_v10_1
			self.iterfileinfo = self.__parse_fileinfos
			self.iterfileremark = self.__parse_fileremarks_none
			self.itercloneremark = self.__parse_cloneremarks_none
		elif v1 == 2:
			self.iteroption = self.__parse_options_v10_2
			self.iterfileinfo = self.__parse_fileinfos_v10_2
			self.iterfileremark = self.__parse_fileremarks
			self.itercloneremark = self.__parse_cloneremarks
		else:
			assert False
		
		return vers
	
	def getformat(self):
		assert self.__lastcalled == "getversion"
		self.__lastcalled = "getformat"
		
		f = self.__file
		
		# scan format
		buf = f.read(4)
		if len(buf) != 4:
			raise IOError, "i/o error in reading format"
		if not(buf == "pa:d" or buf == "pa:s"):
			raise CloneDataInvalidDataError, "invalid format: %s" % buf
		
		return buf
	
	def __parse_options_v10_0(self):
		assert self.__lastcalled == "getformat"
		self.__lastcalled = "iteroption"
		
		f = self.__file
		
		if self.__version != [ 10, 0, 5 ]:
			raise CloneDataVersionMismatchError, "version mismatch"
		
		siz = struct.calcsize("I")
		buf = f.read(siz)
		if len(buf) != siz:
			raise IOError, "i/o error in reading options"
		value = struct.unpack("<I", buf)[0]
		yield "-b", d(str(value))[0]
	
	def __parse_options_v10_1(self):
		assert self.__lastcalled == "getformat"
		self.__lastcalled = "iteroption"
		
		f = self.__file
		d = self.__decode
		
		siz = struct.calcsize("I")
		buf = f.read(siz)
		if len(buf) != siz:
			raise IOError, "i/o error in reading options"
		value = struct.unpack("<I", buf)[0]
		
		if value != 4:
			raise CloneDataInvalidDataError, "invaild option value"
		siz = struct.calcsize("I") * 4
		buf = f.read(siz)
		if len(buf) != siz:
			raise IOError, "i/o error in reading options"
		values = struct.unpack("<IIII", buf)
		yield "-b", d(str(values[0]))
		yield "-s", d(str(values[1]))
		yield "-u", d(str(values[2]))
		yield "-t", d(str(values[3]))
		
	def __parse_options_v10_2(self):
		assert self.__lastcalled == "getformat"
		self.__lastcalled = "iteroption"
		
		f = self.__file
		d = self.__decode
		
		while True:
			buf = f.readline()
			if buf == "\n":
				break
			if len(buf) >= 1 and buf[-1] == '\n':
				buf = buf[:-1]
			pos = buf.find('\t')
			if pos < 0:
				raise CloneDataInvalidDataError, "invalid option value"
			name = d("-" + buf[0:pos])
			value = d(buf[pos + 1:])
			yield name, value
	
	def getpreprocessscript(self):
		assert self.__lastcalled == "iteroption"
		self.__lastcalled = "getpreprocessscript"
		
		f = self.__file
		
		# scan preprocess script
		buf = f.readline()
		if len(buf) >= 1 and buf[-1] == '\n':
			buf = buf[:-1]
		
		return buf
			
	def __parse_fileinfos(self):
		assert self.__lastcalled == "getpreprocessscript"
		self.__lastcalled = "iterfileinfo"
		
		f = self.__file
		d = self.__decode
		
		filepathbuf = ""
		while True:
			buf = f.readline()
			if len(buf) >= 1 and buf[-1] == '\n':
				buf = buf[:-1]
			filepathbuf += buf
			filepathstr = d(filepathbuf)
			siz = struct.calcsize("i")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading file info"
			fileid = struct.unpack("<i", buf)[0]
			siz = struct.calcsize("I")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading file info"
			filelength = struct.unpack("<I", buf)[0]
			
			yield fileid, filepathstr, filelength
			
			buf = f.read(1)
			if len(buf) >= 1 and buf[0] == '\n':
				break
			filepathbuf = buf
	
	def __parse_fileinfos_v10_2(self):
		assert self.__lastcalled == "getpreprocessscript"
		self.__lastcalled = "iterfileinfo"
		
		f = self.__file
		d = self.__decode
		
		while True:
			buf = f.readline()
			if len(buf) >= 1 and buf[-1] == '\n':
				buf = buf[:-1]
			filepathstr = d(buf)
			siz = struct.calcsize("i")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading file remark"
			fileid = struct.unpack("<i", buf)[0]
			siz = struct.calcsize("I")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading file remark"
			filelength = struct.unpack("<I", buf)[0]
			
			if fileid == 0:
				if not(filelength == 0 and len(filepathstr) == 0):
					raise CloneDataInvalidDataError, "invalid file info"
				break
			
			yield fileid, filepathstr, filelength
	
	def __parse_fileremarks(self):
		assert self.__lastcalled == "iterfileinfo"
		self.__lastcalled = "iterfileremark"
		
		f = self.__file
		d = self.__decode
		
		while True:
			buf = f.readline()
			if len(buf) >= 1 and buf[-1] == '\n':
				buf = buf[:-1]
			remarkstr = d(buf)
			siz = struct.calcsize("i")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading file remark"
			fileid = struct.unpack("<i", buf)[0]
			if fileid == 0:
				if len(remarkstr) != 0:
					raise CloneDataInvalidDataError, "invalid file remark"
				break
			
			yield fileid, remarkstr
	
	def __parse_fileremarks_none(self):
		assert self.__lastcalled == "iterfileinfo"
		self.__lastcalled = "iterfileremark"
		return tuple()
	
	def iterclone(self):
		assert self.__lastcalled == "iterfileremark"
		self.__lastcalled = "iterclone"
		
		f = self.__file
		
		while True:
			siz = struct.calcsize("i") * 3
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading clone"
			leftcodefragment = struct.unpack("<iii", buf)
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading clone"
			rightcodefragment = struct.unpack("<iii", buf)
			
			siz = struct.calcsize("Q")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading clone"
			clonesetid = struct.unpack("<Q", buf)[0]
			
			if leftcodefragment == ( 0, 0, 0 ) and rightcodefragment == ( 0, 0, 0 ) and clonesetid == 0:
				break
			
			yield leftcodefragment, rightcodefragment, clonesetid

	def __parse_cloneremarks(self):
		assert self.__lastcalled == "iterclone"
		self.__lastcalled = "itercloneremark"
		
		f = self.__file
		d = self.__decode
		
		while True:
			buf = f.readline()
			if len(buf) >= 1 and buf[-1] == '\n':
				buf = buf[:-1]
			remarkstr = d(buf)
			siz = struct.calcsize("Q")
			buf = f.read(siz)
			if len(buf) != siz:
				raise IOError, "i/o error in reading clone remark"
			clonesetid = struct.unpack("<Q", buf)[0]
			if clonesetid == 0:
				if len(remarkstr) != 0:
					raise CloneDataInvalidDataError, "invalid clone remark"
				break
			
			yield clonesetid, remarkstr
	
	def __parse_cloneremarks_none(self):
		assert self.__lastcalled == "iterclone"
		self.__lastcalled = "itercloneremark"
		return tuple()
	
def prettyprint(input, output):
	encoder = codecs.getencoder(sys.getfilesystemencoding())
	def encode(s): return encoder(s)[0]
	
	reader = CloneDataReader(input)
	
	v = reader.getversion()
	print >> output, "version: ccfx %s" % ".".join(str(n) for n in v)
	
	fmt = reader.getformat()
	if fmt == "pa:d":
		print >> output, "format: pair_diploid"
	else:
		assert False
		
	for n, v in reader.iteroption():
		print >> output, "option: %s %s" % ( encode(n), encode(v) )
	
	p = reader.getpreprocessscript()
	print >> output, "preprocess_script: %s" % encode(p)
	
	print >> output, "source_files {"
	for fileid, filepathstr, filelength in reader.iterfileinfo():
		print >> output, "%d\t%s\t%d" % ( fileid, encode(filepathstr), filelength )
	print >> output, "}"
	
	print >> output, "source_file_remarks {"
	lastfileid = None
	for fileid, remarkstr in reader.iterfileremark():
		if fileid != lastfileid:
			print >> output, "%d:\t%s" % ( fileid, encode(remarkstr) )
			lastfileid = fileid
		else:
			print >> output, ":\t%s" % encode(remarkstr)
	print >> output, "}"
	
	print >> output, "clone_pairs {"
	for leftcodefragment, rightcodefragment, clonesetid in reader.iterclone():
		print >> output, "%d\t%d.%d-%d\t%d.%d-%d" % ( clonesetid, 
				leftcodefragment[0], leftcodefragment[1], leftcodefragment[2],
				rightcodefragment[0], rightcodefragment[1], rightcodefragment[2] )
	print >> output, "}"
	
	print >> output, "clone_set_remarks {"
	lastclonesetid = None
	for clonesetid, remarkstr in reader.itercloneremark():
		if clonesetid != lastclonesetid:
			print >> output, "%d:\t%s" % ( clonesetid, encode(remarkstr) )
			lastcloneid = clonesetid
		else:
			print >> output, ":\t%s" % encode(remarkstr)
	print >> output, "}"

if __name__ == "__main__":
	import sys
	
	if len(sys.argv) == 1:
		print "usage: clonedatareader.py clonedata"
		sys.exit(0)
	
	prettyprint(open(sys.argv[1], "rb"), sys.stdout)

