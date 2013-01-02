% Example using TXL 10.5a source coordinate extensions to extract
% a table of all method definitions with source coordinates

% Jim Cordy, January 2008

% Revised July 2011 - ignore BOM headers in source
% Revised 30.04.08 - unmark embedded blocks - JRC

% Using Python grammar
include "python.grm"

% Ignore BOM headers from Windows
include "bom.grm"

% Redefinitions to collect source coordinates for block definitions as parsed input,
% and to allow for XML markup of block definitions as output

redefine suite
	[block]
    |   [simple_stmt] [endofline]
end redefine

define block
	% input form
        [srcfilename] [srclinenumber]		% Keep track of starting file and line number
        [indent] [endofline]
	    [opt doccomment]
            [repeat stmt_or_newline+]
	    [srcfilename] [srclinenumber] 	% Keep track of ending file and line number
        [dedent]
    |
	% output form
	[opt xml_source_coordinate]
        [indent] [endofline]
            [repeat stmt_or_newline+]
        [dedent]
	[opt end_xml_source_coordinate]
end define

define doccomment
	[longstringlit] [endofline]
    | 	[longcharlit] [endofline]
end define

redefine indent
	[NL] 'INDENT [IN]
    |   [firstindent]
end redefine

define firstindent
	'INDENT [IN]
end define

redefine dedent
	[EX] 'DEDENT [NL]
end redefine

define xml_source_coordinate
    '< [SPOFF] 'source [SP] 'file=[stringlit] [SP] 'startline=[stringlit] [SP] 'endline=[stringlit] '> [SPON] [NL]
end define

define end_xml_source_coordinate
    '< [SPOFF] '/ 'source '> [SPON] [NL]
end define

redefine program
	...
    | 	[repeat block]
end redefine


% Main function - extract and mark up block definitions from parsed input program
function main
    replace [program]
	P [program]
    construct Blocks [repeat block]
    	_ [^ P] 			% Extract all blocks from program
	  [removeEmptyBlocks]		% Get rid of empty ones
	  [convertBlockDefinitions] 	% Mark up with XML
    by 
    	Blocks
end function

rule convertBlockDefinitions
    % Find each block definition and match its input source coordinates
    replace [block]
	FileName [srcfilename] LineNumber [srclinenumber]
        Indent [indent] EOL [endofline] 
	    DocComment [opt doccomment]
            BlockBody [repeat stmt_or_newline+] 
	    EndFileName [srcfilename] EndLineNumber [srclinenumber]
	Dedent [dedent] 

    % Convert file name and line numbers to strings for XML
    construct FileNameString [stringlit]
	_ [quote FileName] 
    construct LineNumberString [stringlit]
	_ [quote LineNumber] 
    construct LineNumberPlus1 [number]
	_ [unquote LineNumberString] [+ 1]
    construct LineNumberPlus1String [stringlit]
	_ [quote LineNumberPlus1] 
    construct EndLineNumberString [stringlit]
	_ [quote EndLineNumber] 
    construct EndLineNumberMinus1 [number]
	_ [unquote EndLineNumberString] [- 1]
    construct EndLineNumberMinus1String [stringlit]
        _ [quote EndLineNumberMinus1]      
    construct FirstIndent [firstindent]
	'INDENT

    % Output is XML form with attributes indicating input source coordinates
    by
	<source file=FileNameString startline=LineNumberPlus1String endline=EndLineNumberMinus1String>
	    FirstIndent EOL 
	        BlockBody [unmarkEmbeddedBlockDefinitions] 
			  [reduceEndOfLines] 
		   	  [reduceEndOfLines2]
			  [reduceEmptyStmts]
	    Dedent 
	</source>
end rule

rule unmarkEmbeddedBlockDefinitions
    replace [block]
	FileName [srcfilename] LineNumber [srclinenumber]
        Indent [indent] EOL [endofline] 
	    DocComment [opt doccomment]
            BlockBody [repeat stmt_or_newline+] 
	    EndFileName [srcfilename] EndLineNumber [srclinenumber]
	Dedent [dedent] 
    by
        Indent EOL 
            BlockBody 
	Dedent 
end rule

rule reduceEndOfLines
    replace [repeat endofline]
	EOL [endofline]
	_ [endofline]
	_ [repeat endofline]
    by
	EOL
end rule

rule reduceEndOfLines2
    replace [opt endofline]
	EOL [endofline]
    by
end rule

rule reduceEmptyStmts
    replace [repeat stmt_or_newline]
	EOL [endofline]
	Stmts [repeat stmt_or_newline]
    by
	Stmts
end rule

rule removeEmptyBlocks
    replace [repeat block]
	FileName [srcfilename] LineNumber [srclinenumber]
        Indent [indent] EOL [endofline] 
	    DocComment [opt doccomment]
            BlockBody [repeat stmt_or_newline+] 
	    EndFileName [srcfilename] EndLineNumber [srclinenumber]
	Dedent [dedent] 
        More [repeat block]
    deconstruct not * [stmt] BlockBody
        Stmt [stmt]
    by
        More
end rule
