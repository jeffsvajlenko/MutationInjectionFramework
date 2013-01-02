% Consistent renaming - Python blocks
% Jim Cordy, May 2010

% Using Python grammar
include "python.grm"

% Redefinition for potential clones
redefine suite
	[block]
    |   [simple_stmt] [endofline]
end redefine

define block
    [indent] [endofline]
	[repeat stmt_or_newline+]
    [dedent]
end define

define potential_clone
    [block]
end define

redefine indent
	[opt newline] 'INDENT [IN]
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
