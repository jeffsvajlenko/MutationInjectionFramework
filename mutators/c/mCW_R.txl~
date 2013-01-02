% Mutate spacing in any program in any language
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
    deconstruct not * [repeat lineitem] P
        Items [repeat lineitem]
    construct Message [id]
        _ [message "*** ERROR: mutate_1_spacing cannot mutate this file"] [quit 99]
end function

% Mutate randomly until something changes
function mutate OriginalP [program]
    replace [program] 
        P [program]
    deconstruct P
        OriginalP
    by
        P [randomlyDeleteSpace1] [randomlyDeleteSpace2] [mutate OriginalP]
end function

% For each space between tokens in the input, randomly delete the space
rule randomlyDeleteSpace1
    import N [number]
	import CheckN [number]
    replace $ [repeat lineitem]
        Item1 [lineitem] Item2 [space] Item3 [lineitem] MoreItems [repeat lineitem]
    % Leading space is another case
    deconstruct not Item1
        _ [space]
    % If the separated tokens are identifiers (including keywords) or numbers, don't unseparate them
    where not
	Item1 [bothIdNumber Item3]
    construct Random [number]
	_ [rand N]
	deconstruct not CheckN
		1
	export CheckN
		Random
    deconstruct Random
        1
    by
        Item1 Item3 MoreItems
end rule

function bothIdNumber Item [lineitem]
    deconstruct Item
	_ [id_or_number]
    match [lineitem]
        _ [id_or_number]
end function

% For each leading space in the input, randomly delete the space
rule randomlyDeleteSpace2
    import N [number]
	import CheckN [number]
    replace $ [line]
        _ [space] Items [repeat lineitem] Newline [newline]
    construct Random [number]
	_ [rand N]
	deconstruct not CheckN
		1
	export CheckN
		Random
    deconstruct Random
        1
    by
        Items Newline
end rule

