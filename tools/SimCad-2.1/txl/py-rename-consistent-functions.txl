% Consistent renaming - Python functions
% Jim Cordy, May 2010

% Using Python grammar
include "python.grm"

% Redefinition for potential clones
define block_funcdef
    'def [id] [parameters] ': 
    [indent] [endofline] 
	[repeat fstmt+] 
    [dedent] 
end define

define fstmt
    [repeat newline] [stmt]
end define

define potential_clone
    [block_funcdef]
end define

redefine indent
	[newline] 'INDENT [IN]
end redefine

redefine dedent
	[EX] 'DEDENT [newline]
end redefine

% Generic consistent renaming
include "generic-rename-consistent.txl"

% Specialize for Python
redefine xml_source_coordinate
    '< [SPOFF] 'source [SP] 'file=[stringlit] [SP] 'startline=[stringlit] [SP] 'endline=[stringlit] '> [SPON] [newline]
end redefine

redefine end_xml_source_coordinate
    '< [SPOFF] '/ 'source '> [SPON] [newline]
end redefine
