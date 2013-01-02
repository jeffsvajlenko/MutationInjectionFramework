-- Experiment Properties --
CREATE TABLE IF NOT EXISTS properties (
	id INTEGER DEFAULT 1 NOT NULL CHECK (id = 1),
	
	-- Experiment Language, java=0, c=1, c#=2
	language INTEGER DEFAULT 0 CHECK(language = 0 OR language = 1 OR language = 2),
	
	-- Type of Generation: automatic(0) or manual(1)
	generation_type INTEGER DEFAULT 0 CHECK(generation_type = 0 OR generation_type = 1),
	
	-- Experiment Stage
	--	0 = Experiment Setup Stage
	--	1 = Generation Stage
	--	2 = Evaluation Setup Stage
	--	3 = Evaluation Stage
	--	4 = Results Stage
	experiment_stage INTEGER DEFAULT 0 CHECK (experiment_stage = -1 OR experiment_stage = 0 OR experiment_stage = 1 OR experiment_stage = 2 OR experiment_stage = 3 OR experiment_stage = 4),
	
-- Generation Properties (if automatic generation) --
	-- Type of Fragments: function(0) or block(1)
	fragment_type INTEGER DEFAULT 0 CHECK (fragment_type = 0 OR fragment_type = 1),
	
	-- Minimum Clone Fragment Size in Lines --
	fragment_min_size_lines INTEGER DEFAULT 15 CHECK (fragment_min_size_lines > 0),
	
	-- Maximum Clone Fragment Size in Lines --
	fragment_max_size_lines INTEGER DEFAULT 50 CHECK (fragment_max_size_lines > 0),
	
	-- Minimum Clone Fragment Size in Tokens --
	fragment_min_size_tokens INTEGER DEFAULT 1 CHECK (fragment_min_size_tokens > 0),
	
	-- Maximum Clone Fragment Size in Tokens --
	fragment_max_size_tokens INTEGER DEFAULT 100 CHECK (fragment_max_size_tokens > 0),
	
	-- Number of Times to Mutate Each Selected Fragment --
	--mutation_number INTEGER DEFAULT 1 CHECK (mutation_number > 0),
	
	-- Number of Times to Inject Each Generated Clone --
	injection_number INTEGER DEFAULT 1 CHECK (injection_number > 0),

	-- Allowed Difference Between Fragments of Generated Clones --
	allowed_fragment_difference DOUBLE PRECISION DEFAULT 0.30 CHECK (allowed_fragment_difference >= 0.0 AND allowed_fragment_difference <= 1.0),
	
	-- Containment of Mutations In Generated Fragment of Generated Clones (% of lines of code at start/end not modified) --
	mutation_containment DOUBLE PRECISION DEFAULT 0.2 CHECK (mutation_containment >= 0.0 AND mutation_containment <= 1.0),
	
	-- Number of Times to Attempt Fragment Mutation Before Giving Up --
	mutation_attempts INTEGER DEFAULT 10 CHECK (mutation_attempts > 0),
	
	-- Number of Times to Attempt Mutation Operator Before Giving Up --
	operator_attempts INTEGER DEFAULT 10 CHECK (operator_attempts > 0),
	
	-- Maximum Number of Fragments to Create Clones From --
	max_fragments INTEGER DEFAULT 1 CHECK (max_fragments >= 0),
	
-- Evaluation Properties --	
	
	-- Subsume matcher tolerance for determining if a found clone matches a known clone (allownace for imperfect subsume match) --
	subsume_matcher_tolerance DOUBLE PRECISION DEFAULT 0.15 CHECK (subsume_matcher_tolerance >= 0.0 AND subsume_matcher_tolerance <= 1.0),

	-- Required similarity for a clone to be considered a clone in the recall evaluation --
	recall_required_similarity DOUBLE PRECISIOn DEFAULT 0.5 CHECK (recall_required_similarity >= 0.0 AND recall_required_similarity <= 1.0),
	
	-- Required similarity for a clone to be considered a clone in the precision evaluation --
	precision_required_similarity DOUBLE PRECISION DEFAULT 0.5 CHECK (precision_required_similarity >= 0.0 AND precision_required_similarity <= 1.0),
	
	-- Which verifier should be used, token, line, either or both (must pass both verifiers) --
	verifier_mode VARCHAR(50) DEFAULT 'either' CHECK(verifier_mode = 'line' OR verifier_mode = 'token' OR verifier_mode = 'either' OR verifier_mode = 'both'),
	
	
	CHECK(fragment_min_size_lines <= fragment_max_size_lines),
	CHECK(fragment_max_size_lines >= fragment_min_size_lines),
	CHECK(fragment_min_size_tokens <= fragment_max_size_tokens),
	CHECK(fragment_max_size_tokens >= fragment_min_size_tokens),
	
	PRIMARY KEY(id)
);


------------------------
-- Mutation Operators --
------------------------
-- number generator for operator ids
CREATE SEQUENCE IF NOT EXISTS operators_operator_id;

-- operators table
CREATE TABLE IF NOT EXISTS mutation_operators (
	operator_id			INTEGER			DEFAULT nextval('operators_operator_id'),
	operator_name 		VARCHAR(20) 	NOT NULL,
	description 		VARCHAR(5000) 	NOT NULL,
	target_clone_type 	INTEGER 		CHECK (target_clone_type >= 1 and target_clone_type <= 4),
	mutator 			VARCHAR(1000) 	NOT NULL,
	
	PRIMARY KEY (operator_id)
	
);



--------------
-- Mutators --
--------------

-- number generator for mutator ids
CREATE SEQUENCE IF NOT EXISTS mutators_mutator_id;

-- base mutator table, stores definitional information
CREATE TABLE IF NOT EXISTS mutators (
	
	mutator_id			INTEGER 		DEFAULT nextval('mutators_mutator_id'),
	description			VARCHAR(5000) 	NOT NULL,
	target_clone_type	INTEGER 		CHECK (target_clone_type >= 1 and target_clone_type <= 4),
	
	PRIMARY KEY (mutator_id)
	
);

-- stores mutation operators in mutator, and their order
CREATE TABLE IF NOT EXISTS mutator_operators (
	
	mutator_id 		INTEGER			NOT NULL,
	operator_num 	INTEGER			NOT NULL,
	operator_id 	INTEGER			NOT NULL,
	
	PRIMARY KEY (mutator_id, operator_num),
	 
	FOREIGN KEY (operator_id) REFERENCES mutation_operators ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (mutator_id) REFERENCES mutators ON UPDATE CASCADE ON DELETE CASCADE
);



---------------
-- Fragments --
---------------

-- number generator for fragment id
CREATE SEQUENCE IF NOT EXISTS fragments_fragment_id;

CREATE TABLE IF NOT EXISTS fragments (

	fragment_id 	INTEGER 		DEFAULT nextval('fragments_fragment_id'),
	srcfile 		VARCHAR(1000) 	NOT NULL,
	fragmentfile	VARCHAR(1000)	NOT NULL,
	startline 		INTEGER 		NOT NULL CHECK (startline >= 0) ,
	endline 		INTEGER 		NOT NULL CHECK (endline >= 0),
	
	PRIMARY KEY (fragment_id)
	
);



----------------------
-- Mutant Fragments --
----------------------

-- number generator for mutator id
CREATE SEQUENCE IF NOT EXISTS mutant_fragments_mutant_id;

CREATE TABLE IF NOT EXISTS mutant_fragments (

	mutant_id 		INTEGER			DEFAULT nextval('mutant_fragments_mutant_id'),
	fragment_id 	INTEGER 		NOT NULL,
	mutator_id 		INTEGER 		NOT NULL,
	srcfile 		VARCHAR(1000) 	NOT NULL,
	
	PRIMARY KEY (mutant_id),
	
	FOREIGN KEY (fragment_id) REFERENCES fragments ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (mutator_id)  REFERENCES mutators ON UPDATE CASCADE ON DELETE CASCADE
	
);



-----------------
-- Mutant Base --
-----------------

-- number generator for mutant base id
CREATE SEQUENCE IF NOT EXISTS mutant_bases_base_id;

CREATE TABLE IF NOT EXISTS mutant_bases (

	base_id 			INTEGER 			DEFAULT nextval('mutant_bases_base_id'),
	directory 			VARCHAR(1000)		NOT NULL,
	mutant_id 			INTEGER 			NOT NULL,
	isrcfile 			VARCHAR(1000) 		NOT NULL,
	istartline 			INTEGER 			NOT NULL,
	iendline 			INTEGER 			NOT NULL,
	osrcfile 			VARCHAR(1000) 		NOT NULL,
	ostartline 			VARCHAR(1000) 		NOT NULL,
	oendline 			VARCHAR(1000) 		NOT NULL,
	
	PRIMARY KEY (base_id),
	
	FOREIGN KEY (mutant_id) REFERENCES mutant_fragments ON UPDATE CASCADE ON DELETE CASCADE
	
);



-----------
-- Tools --
-----------

-- number generator for tool id
CREATE SEQUENCE IF NOT EXISTS tools_tool_id;

CREATE TABLE IF NOT EXISTS tools (

	tool_id			INTEGER				DEFAULT nextval('tools_tool_id'),
	name			VARCHAR(100) 		NOT NULL,
	directory		VARCHAR(1000) 		NOT NULL,
	description		VARCHAR(1000) 		NOT NULL,
	toolrunner		VARCHAR(1000) 		NOT NULL,
	
	PRIMARY KEY (tool_id)

);



-----------------------------
-- Clone Detection Reports --
-----------------------------

CREATE TABLE IF NOT EXISTS clone_detection_reports (
	
	tool_id 	INTEGER			NOT NULL,
	base_id 	INTEGER			NOT NULL,
	report		VARCHAR(1000)		NOT NULL,
	
	PRIMARY KEY (tool_id, base_id),
	
	FOREIGN KEY (tool_id) REFERENCES tools ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (base_id) REFERENCES mutant_bases ON UPDATE CASCADE ON DELETE CASCADE,
	
	
);



-----------------
-- Unit Recall --
-----------------

CREATE TABLE IF NOT EXISTS unit_recall (

	tool_id			INTEGER				NOT NULL,
	base_id 		INTEGER				NOT NULL,
	recall 			DOUBLE PRECISION	NOT NULL,
    srcfile1 		VARCHAR(1000),
    startline1 		INTEGER,
    endline1		INTEGER,
    srcfile2 		VARCHAR(1000),
    startline2 		INTEGER,
    endline2 		INTEGER,
    
	PRIMARY KEY (tool_id, base_id),
	
	FOREIGN KEY (tool_id) REFERENCES tools ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (base_id) REFERENCES mutant_bases ON UPDATE CASCADE ON DELETE CASCADE,
	
	--Clone specification is allowed to be null (only in entirity) if recall is 0.0
	CHECK(
				(
	       			(srcfile1 is NOT NULL) AND (startline1 IS NOT NULL) AND (endline1 IS NOT NULL) AND
		   			(srcfile2 is NOT NULL) AND (startline2 IS NOT NULL) AND (endline2 IS NOT NULL)
		   		) 
			OR 
		  		(
		  			(srcfile1 is NULL) AND (startline1 IS NULL) AND (endline1 IS NULL) AND
					(srcfile2 is NULL) AND (startline2 IS NULL) AND (endline2 IS NULL) AND
					(recall=0.0)
		  		)
		 )
	
);



--------------------
-- Unit Precision --
--------------------

-- base table for unit precision
CREATE TABLE IF NOT EXISTS unit_precision (

	tool_id			INTEGER,
	base_id 		INTEGER,
	precision 		DOUBLE PRECISION 		NOT NULL,

	PRIMARY KEY (tool_id, base_id),

	FOREIGN KEY (tool_id) REFERENCES tools ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (base_id) REFERENCES mutant_bases ON UPDATE CASCADE ON DELETE CASCADE
	
);

-- number generator for unit precision clones considered
CREATE SEQUENCE IF NOT EXISTS unit_precision_clones_id;

-- table containing the clones considered when evaluating unit precision
CREATE TABLE IF NOT EXISTS unit_precision_clones_considered (

	id 					INTEGER 			DEFAULT nextval('unit_precision_clones_id'),
	tool_id 			INTEGER 			NOT NULL,
	base_id 			INTEGER 			NOT NULL,
	isclone 			BOOLEAN 			NOT NULL,
	verifiersuccess 	BOOLEAN				NOT NULL,
	srcfile1 			VARCHAR(100) 		NOT NULL,
    startline1 			INTEGER 			NOT NULL,
    endline1 			INTEGER 			NOT NULL,
    srcfile2 			VARCHAR(100) 		NOT NULL,
    startline2 			INTEGER 			NOT NULL,
    endline2 			INTEGER 			NOT NULL,
    
    PRIMARY KEY (id),
    
	FOREIGN KEY (tool_id) REFERENCES tools ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (base_id) REFERENCES mutant_bases ON UPDATE CASCADE ON DELETE CASCADE
	
);

-- operators --
	-- type1:
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mNC', 'no change', 1, 'mNC');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCW_A', 'changes in whitespace (space added)', 1, 'mCW_A');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCW_R', 'changes in whitespace (space removed)', 1, 'mCW_R');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCC_EOL', 'changes in comments (//)', 1, 'mCC_EOL');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCC_BT', 'changes in comments (/* */)', 1, 'mCC_BT');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCF_A', 'changes in formating (newline added)', 1, 'mCF_A');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mCF_R', 'changes in formating (newline removed)', 1, 'mCF_R');

	-- type2:
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mSRI', 'semantic renaming of identifiers', 2, 'mSRI');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mARI', 'arbitrary renaming of identifiers', 2, 'mARI');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mRL_N', 'arbitrary change of an literal (numeric)', 2, 'mRL_N');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mRL_S', 'arbitrary change of an literal (string)', 2, 'mRL_S');

	-- type3:
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mRPE', 'replacement of identifiers with expressions', 3, 'mRPE');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mSIL', 'small insertion within a line', 3, 'mSIL');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mSDL', 'small deletion within a line', 3, 'mSDL');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mIL', 'insertion of a line', 3, 'mIL');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mDL', 'deletion of a line', 3, 'mDL');
--MERGE INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES ('mML', 'modification of a line', 3, 'mML');
