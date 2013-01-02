% Random number module for TXL
% This module provides the function [rand N],
% which yields a random number between 1 and N on each call
% Note: in this implementation, N must be between 2 and 10000

% The generator must be initialized once by a call to 
% [randinit] before [rand] can be used

% The algorithm is a linear congruential generator (LCG) of
% maximal period, initialized by a call the the operating system
% to choose a random seed.

function randinit
    % Three digit prime numbers that make appropriate seeds
    construct Seeds [repeat number]
        157 163 167 173 179 181 191 193 197 199 
	211 223 227 229 233 239 241 251 257 263 
	269 271 277 281 283 293 307 311 313 317 
	331 337 347 349 353 359 367 373 379 383 
	389 397 401 409 419 421 431 433 439 443 
	449 457 461 463 467 479 487 491 499 503 
	509 521 523 541 547 557 563 569 571 577 
	587 593 599 601 607 613 617 619 631 641 
	643 647 653 659 661 673 677 683 691 701 
	709 719 727 733 739 743 751 757 761 769 
    % Ask Unix/Linux for a random number
    construct _ [number]
	_ [system "bash -c 'echo $RANDOM' > _rand_"]
    % Use it to choose one of the primes as seed
    construct RandomIndex [number]
	_ [read "_rand_"] [rem 100] [+ 1]
    % Clean up the temp file
    construct _ [number]
    	_ [system "/bin/rm -f _rand_"]
    % Now choose our prime seed
    construct RandomSeed [repeat number]
        Seeds [select RandomIndex RandomIndex]
    deconstruct RandomSeed
    	Seed [number]
    export Seed
    match [any]
        _ [any]
end function

function rand Upper [number]
    % Generate the next random number using a prime multiplier and modulus
    % The result is always odd, therefore cannot yield 0 from an even modulus
    import Seed [number]
    export Seed 
        Seed [* 21] [+ 17] [rem 10000]
    % Transform it using a prime divisor modulo Upper, shifted to 1-origin
    % Yields a random number between 1 and Upper that may be either even or odd
    replace [number]
        _ [number]
    by
	Seed [rem Upper]
end function
