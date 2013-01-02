% Mutate by systematic replacement of one identifier by an expression in any program in any C-like language
#pragma -char -comment

% C tokens, so we don't break them up
include "jtokens.grm"

% Separate sequences of spaces and tabs so they cn be individually added or deleted
tokens
    space	" "
	    |	"	"
end tokens

% Our grammar simply processes lines of text
define program
    [repeat line]
    [repeat lineitem] 	% Some files don't have a final newline
end define

define line
    [repeat lineitem] [newline]
end define

define lineitem
	[space_comment]
    |	[space]
    |	[type]
    |	[assignment_op]
    |	[expression]
    |	[id_or_number]
    |	[not newline] [token]
end define

define space_comment
	[repeat space+] [comment]
end define

define id_or_number
	[id]
    |	[number]
    |	[key]
end define

define type
	'int | 'float | 'char | 'class | 'void | [id] [opt space] '*
end define

define expression
	[id] [not selector]
    |	'( [id] [op] [id] ')
end define

define selector
	'( | '. | '-> | '[
end define

define op
	'+ | '- | '* 
end define

define assignment_op
	'= | '+= | '-= | '*= | '/= | '++ | '--
end define

% We need to mutate randomly, so use the new TXL random module (Linux/Unix only, no Windows)
include "random.mod"

% Main rule - initialize everything then mutate
function main
    % Probability of mutation is 1/N, that is, 1 in N instances will be mutated
    export N [number]
        5000
    % We need an example of a space to insert 
    construct SpaceNewline [opt line]
        _ [parse " "]
    deconstruct SpaceNewline
        Space [space] Newline [newline]
    export Space 
    export Newline
    % Initialize the random number seed
    construct _ [id]
        _ [pragma "-token"] [randinit] [pragma "-char"]
    % Apply mutators
    replace [program]
        P [program]
    by
        P [checkMutatable]
	  [mutate P]
end function

% Can we mutate this input?
function checkMutatable
    match [program]
        P [program]
    deconstruct * [line] P
        Line [line]
    deconstruct not * [type] Line
	_ [type]
    deconstruct * [repeat lineitem] Line
        PreItem [lineitem] _ [id] _ [repeat lineitem]
    deconstruct not PreItem
    	'.
    construct Message [id]
        _ [message "*** ERROR: mutate_5_systematic_expression cannot mutate this file"] [quit 99]
end function

% Mutate randomly until something changes
function mutate OriginalP [program]
    replace [program] 
        P [program]
    deconstruct P
        OriginalP
    by
        P [randomlySystematicallyRenameSomeId]
	  [mutate OriginalP]
end function

% Find some identifier to rename
function randomlySystematicallyRenameSomeId
    import N [number]
    replace [program]
        P [program]
    % Get all the ids in the program
    construct Ids [repeat id]
        _ [^ P] 
    % Choose an id
    construct TheIds [repeat id]
        Ids [chooseOne]
    deconstruct TheIds 
    	TheId [id]
    by
        P [replaceWithExpression TheId]
	  [cleanupDoubleParens]
	  [cleanupParenSubscripts]
end function

rule replaceWithExpression TheId [id]
    replace $ [line]
    	Items [repeat lineitem] EOL [newline]
    deconstruct not * [lineitem] Items
    	Type [type]
    by
        Items [substituteExpression TheId] EOL
end rule

function substituteExpression TheId [id]
    replace * [repeat lineitem]
        PreItem [lineitem] TheId MoreItems [repeat lineitem]
    deconstruct not PreItem
    	'.
    deconstruct not PreItem
    	'*
    deconstruct not * [assignment_op] MoreItems
    	_ [assignment_op]
    construct TheExpression [expression]
    	'( TheId '* TheId ')
    by
        PreItem TheExpression MoreItems
end function

function chooseOne
    replace [repeat id]
        Ids [repeat id]
    % Find out how many there are
    construct Nids [number]
    	_ [length Ids]
    % [rand] randomly generates a number between 1 and Nids
    construct Random [number]
	_ [rand Nids]
    % Pick that identifier
    construct TheIds [repeat id]
        Ids [select Random Random]
    deconstruct TheIds
	TheId [id]
    % Make sure that it appears more than once
    deconstruct * Ids
        TheId MoreIds [repeat id]
    deconstruct * MoreIds
        TheId _ [repeat id]
    by
        TheId
end function

rule cleanupDoubleParens
    replace [repeat lineitem]
        '( E [expression] ') More [repeat lineitem]
    deconstruct E
        '( _ [id] '* _ [id] ')
    by
        E More
end rule

rule cleanupParenSubscripts
    replace [repeat lineitem]
        '[ E [expression] '] More [repeat lineitem]
    deconstruct E
        '( Id1 [id] '* Id2 [id] ')
    by
        '[ Id1 '* Id2 '] More
end rule
