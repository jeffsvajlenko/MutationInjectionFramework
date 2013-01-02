% Mutate with small in-line insertion in any program in any C-like language
#pragma -char -comment

% C tokens, so we don't break them up
include "cstokens.grm"

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
    |	[key] [opt space] [opt '(]	% prevent this one from being a parenthesized_item?
    |	[id_or_number]
    | 	[parenthesized_item]
    |	[not newline] [token]
end define

define space_comment
	[repeat space+] [comment]
end define

define parenthesized_item
	'( [list parenitem] ')
end define

define parenitem
	[repeat paren_subitem+]
end define

define paren_subitem
	[not ',] [not ')] [lineitem]
end define

define id_or_number
	[id]
    |	[number]
    |	[key]
end define

% We need to mutate randomly, so use the new TXL random module (Linux/Unix only, no Windows)
include "random.mod"

% Main rule - initialize everything then mutate
function main
    % Probability of mutation is 1/N, that is, 1 in N instances will be mutated
    export N [number]
    	10000
    export CheckN [number]
	0
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
    deconstruct not * [list parenitem] P
        Items [list parenitem]
    construct Message [id]
        _ [message "*** ERROR: mutate_7_small_insertion cannot mutate this file"] [quit 99]
end function

% Mutate randomly until something changes
function mutate OriginalP [program]
    replace [program] 
        P [program]
    deconstruct P
        OriginalP
    by
        P [randomlyMakeSmallInsertion]
	  [mutate OriginalP]
end function

% For each pair of tokens in the input, randomly add a space between them
rule randomlyMakeSmallInsertion
    import Space [space]
    import N [number]
	import CheckN [number]
    replace $ [list parenitem]
        Items [list parenitem]
    % [rand] randomly generates a number between 1 and N
    construct Random [number]
	_ [rand N]
    % The probability of 1 is 1/N
	deconstruct not CheckN
		1
	export CheckN
		Random
    deconstruct Random
        1
    % Cconstruct a renamed id
    construct NewId [id]
        _ [unquote "X"] [!]
    construct NewItem [list parenitem]
        NewId
    by
        NewItem [, Items]
end rule
