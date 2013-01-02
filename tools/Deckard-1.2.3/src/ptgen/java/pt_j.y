
%pure-parser

%{
#include<ptree.h>

using namespace std;
%}

%union{
Tree *t;
}

%{
void yyerror(char*s);
int yylex(YYSTYPE *yylvalp);

Tree *root;
%}


%type <t> expression_statement
%type <t> variable_declarators
%type <t> post_increment_expression
%type <t> explicit_constructor_invocation
%type <t> throw_statement
%type <t> block_begin
%type <t> import_declaration
%type <t> post_decrement_expression
%type <t> switch_block_statement_groups
%type <t> assert_statement
%type <t> inclusive_or_expression
%type <t> for_statement_nsi
%type <t> while_statement
%type <t> statement_expression
%type <t> assign_any
%type <t> label_decl
%type <t> formal_parameter_list
%type <t> for_statement
%type <t> local_variable_declaration_statement
%type <t> block_end
%type <t> compilation_unit
%type <t> abstract_method_declaration
%type <t> trap_overflow_corner_case
%type <t> class_instance_creation_expression
%type <t> static_initializer
%type <t> catches
%type <t> name
%type <t> something_dot_new
%type <t> constructor_header
%type <t> variable_declarator_id
%type <t> pre_decrement_expression
%type <t> identifier
%type <t> constructor_block_end
%type <t> constructor_declarator
%type <t> switch_expression
%type <t> interface_declaration
%type <t> unary_expression
%type <t> interface_type
%type <t> left_hand_side
%type <t> break_statement
%type <t> unary_expression_not_plus_minus
%type <t> static
%type <t> labeled_statement
%type <t> dim_exprs
%type <t> class_or_interface_type
%type <t> switch_statement
%type <t> assignment_expression
%type <t> type_declarations
%type <t> interface_member_declaration
%type <t> anonymous_class_creation
%type <t> array_access
%type <t> package_declaration
%type <t> reference_type
%type <t> switch_labels
%type <t> variable_declarator
%type <t> continue_statement
%type <t> argument_list
%type <t> assignment
%type <t> interface_type_list
%type <t> conditional_or_expression
%type <t> multiplicative_expression
%type <t> field_access
%type <t> class_type
%type <t> interfaces
%type <t> catch_clause
%type <t> statement_without_trailing_substatement
%type <t> for_update
%type <t> statement_expression_list
%type <t> array_type
%type <t> single_type_import_declaration
%type <t> super
%type <t> primary_no_new_array
%type <t> method_body
%type <t> class_member_declaration
%type <t> if_then_else_statement_nsi
%type <t> interface_body
%type <t> conditional_and_expression
%type <t> pre_increment_expression
%type <t> assignment_operator
%type <t> do_statement_begin
%type <t> expression
%type <t> throws
%type <t> block
%type <t> if_then_else_statement
%type <t> exclusive_or_expression
%type <t> float
%type <t> integral
%type <t> primary
%type <t> for_init
%type <t> synchronized_statement
%type <t> if_then_statement
%type <t> array_creation_expression
%type <t> extends_interfaces
%type <t> for_begin
%type <t> simple_name
%type <t> method_declaration
%type <t> finally
%type <t> block_statement
%type <t> shift_expression
%type <t> primitive_type
%type <t> literal
%type <t> class_body_declaration
%type <t> variable_initializer
%type <t> statement
%type <t> method_invocation
%type <t> type
%type <t> final
%type <t> import_declarations
%type <t> relational_expression
%type <t> qualified_name
%type <t> formal_parameter
%type <t> class_body
%type <t> catch_clause_parameter
%type <t> switch_label
%type <t> equality_expression
%type <t> type_import_on_demand_declaration
%type <t> modifiers
%type <t> type_literals
%type <t> method_header
%type <t> for_header
%type <t> empty_statement
%type <t> labeled_statement_nsi
%type <t> field_declaration
%type <t> constant_expression
%type <t> local_variable_declaration
%type <t> modifier
%type <t> class_body_declarations
%type <t> synchronized
%type <t> type_declaration
%type <t> interface_member_declarations
%type <t> try_statement
%type <t> and_expression
%type <t> goal
%type <t> dim_expr
%type <t> while_statement_nsi
%type <t> cast_expression
%type <t> postfix_expression
%type <t> return_statement
%type <t> do_statement
%type <t> constant_declaration
%type <t> block_statements
%type <t> variable_initializers
%type <t> class_type_list
%type <t> constructor_body
%type <t> dims
%type <t> additive_expression
%type <t> switch_block_statement_group
%type <t> array_initializer
%type <t> this_or_super
%type <t> statement_nsi
%type <t> class_declaration
%type <t> while_expression
%type <t> switch_block
%type <t> conditional_expression
%type <t> method_declarator
%type <t> constructor_declaration
%type <t> STRING_LIT_TK
%type <t> OR_ASSIGN_TK
%type <t> REL_CL_TK
%type <t> THROW_TK
%type <t> ASSIGN_TK
%type <t> CCB_TK
%type <t> PUBLIC_TK
%type <t> FOR_TK
%type <t> INSTANCEOF_TK
%type <t> BOOL_LIT_TK
%type <t> PRIVATE_TK
%type <t> IF_TK
%type <t> CASE_TK
%type <t> REM_TK
%type <t> CHAR_TK
%type <t> PAD_TK
%type <t> CATCH_TK
%type <t> ASSERT_TK
%type <t> OCB_TK
%type <t> BOOLEAN_TK
%type <t> THROWS_TK
%type <t> SUPER_TK
%type <t> ABSTRACT_TK
%type <t> LT_TK
%type <t> LTE_TK
%type <t> STRICT_TK
%type <t> NEQ_TK
%type <t> SRS_ASSIGN_TK
%type <t> CP_TK
%type <t> DOT_TK
%type <t> NULL_TK
%type <t> REM_ASSIGN_TK
%type <t> BREAK_TK
%type <t> DEFAULT_TK
%type <t> TRANSIENT_TK
%type <t> NOT_TK
%type <t> FP_LIT_TK
%type <t> CONTINUE_TK
%type <t> AND_ASSIGN_TK
%type <t> DOUBLE_TK
%type <t> MULT_TK
%type <t> PLUS_ASSIGN_TK
%type <t> SRS_TK
%type <t> ELSE_TK
%type <t> INTEGRAL_TK
%type <t> ZRS_TK
%type <t> IMPORT_TK
%type <t> INTERFACE_TK
%type <t> PACKAGE_TK
%type <t> OSB_TK
%type <t> VOID_TK
%type <t> FINAL_TK
%type <t> PROTECTED_TK
%type <t> FP_TK
%type <t> FLOAT_TK
%type <t> NEG_TK
%type <t> DO_TK
%type <t> STATIC_TK
%type <t> MODIFIER_TK
%type <t> GTE_TK
%type <t> SWITCH_TK
%type <t> BOOL_OR_TK
%type <t> OR_TK
%type <t> BYTE_TK
%type <t> LS_TK
%type <t> INT_TK
%type <t> INCR_TK
%type <t> VOLATILE_TK
%type <t> THIS_TK
%type <t> C_TK
%type <t> ZRS_ASSIGN_TK
%type <t> REL_QM_TK
%type <t> SHORT_TK
%type <t> XOR_TK
%type <t> NATIVE_TK
%type <t> RETURN_TK
%type <t> LONG_TK
%type <t> MINUS_TK
%type <t> MULT_ASSIGN_TK
%type <t> DECR_TK
%type <t> INT_LIT_TK
%type <t> TRY_TK
%type <t> SYNCHRONIZED_TK
%type <t> OP_TK
%type <t> GT_TK
%type <t> MINUS_ASSIGN_TK
%type <t> SC_TK
%type <t> FINALLY_TK
%type <t> EQ_TK
%type <t> IMPLEMENTS_TK
%type <t> AND_TK
%type <t> DIV_TK
%type <t> ID_TK
%type <t> CONST_TK
%type <t> CHAR_LIT_TK
%type <t> WHILE_TK
%type <t> CLASS_TK
%type <t> CSB_TK
%type <t> LS_ASSIGN_TK
%type <t> PLUS_TK
%type <t> XOR_ASSIGN_TK
%type <t> DIV_ASSIGN_TK
%type <t> EXTENDS_TK
%type <t> BOOL_AND_TK
%type <t> NEW_TK
%token   PLUS_TK         MINUS_TK        MULT_TK         DIV_TK    REM_TK
%token   LS_TK           SRS_TK          ZRS_TK
%token   AND_TK          XOR_TK          OR_TK
%token   BOOL_AND_TK BOOL_OR_TK
%token   EQ_TK NEQ_TK GT_TK GTE_TK LT_TK LTE_TK

/* This maps to the same binop_lookup entry than the token above */

%token   PLUS_ASSIGN_TK  MINUS_ASSIGN_TK MULT_ASSIGN_TK DIV_ASSIGN_TK
%token   REM_ASSIGN_TK
%token   LS_ASSIGN_TK    SRS_ASSIGN_TK   ZRS_ASSIGN_TK
%token   AND_ASSIGN_TK   XOR_ASSIGN_TK   OR_ASSIGN_TK


/* Modifier TOKEN have to be kept in this order. Don't scramble it */

%token   PUBLIC_TK       PRIVATE_TK         PROTECTED_TK
%token   STATIC_TK       FINAL_TK           SYNCHRONIZED_TK
%token   VOLATILE_TK     TRANSIENT_TK       NATIVE_TK
%token   PAD_TK          ABSTRACT_TK        STRICT_TK
%token   MODIFIER_TK

/* Keep those two in order, too */
%token   DECR_TK INCR_TK

/* From now one, things can be in any order */

%token   DEFAULT_TK      IF_TK              THROW_TK
%token   BOOLEAN_TK      DO_TK              IMPLEMENTS_TK
%token   THROWS_TK       BREAK_TK           IMPORT_TK
%token   ELSE_TK         INSTANCEOF_TK      RETURN_TK
%token   VOID_TK         CATCH_TK           INTERFACE_TK
%token   CASE_TK         EXTENDS_TK         FINALLY_TK
%token   SUPER_TK        WHILE_TK           CLASS_TK
%token   SWITCH_TK       CONST_TK           TRY_TK
%token   FOR_TK          NEW_TK             CONTINUE_TK
%token   GOTO_TK         PACKAGE_TK         THIS_TK
%token   ASSERT_TK

%token   BYTE_TK         SHORT_TK           INT_TK            LONG_TK
%token   CHAR_TK         INTEGRAL_TK

%token   FLOAT_TK        DOUBLE_TK          FP_TK

%token   ID_TK

%token   REL_QM_TK         REL_CL_TK NOT_TK  NEG_TK

%token   ASSIGN_ANY_TK   ASSIGN_TK
%token   OP_TK  CP_TK  OCB_TK  CCB_TK  OSB_TK  CSB_TK  SC_TK  C_TK DOT_TK

%token   STRING_LIT_TK   CHAR_LIT_TK        INT_LIT_TK        FP_LIT_TK
%token   TRUE_TK         FALSE_TK           BOOL_LIT_TK       NULL_TK



%%


goal : compilation_unit 
    {
        $$= new NonTerminal( 132 );

        $$->addChild($1);

        $1->parent= $$;
root= $$;

    }
    ;

literal : INT_LIT_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

literal : FP_LIT_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

literal : BOOL_LIT_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

literal : CHAR_LIT_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

literal : STRING_LIT_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

literal : NULL_TK 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type : primitive_type 
    {
        $$= new NonTerminal( 105 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type : reference_type 
    {
        $$= new NonTerminal( 105 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primitive_type : integral 
    {
        $$= new NonTerminal( 99 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primitive_type : float 
    {
        $$= new NonTerminal( 99 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primitive_type : BOOLEAN_TK 
    {
        $$= new NonTerminal( 99 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : BYTE_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : SHORT_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : INT_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : LONG_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : CHAR_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

integral : INTEGRAL_TK 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

float : FLOAT_TK 
    {
        $$= new NonTerminal( 85 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

float : DOUBLE_TK 
    {
        $$= new NonTerminal( 85 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

float : FP_TK 
    {
        $$= new NonTerminal( 85 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

reference_type : class_or_interface_type 
    {
        $$= new NonTerminal( 52 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

reference_type : array_type 
    {
        $$= new NonTerminal( 52 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_or_interface_type : name 
    {
        $$= new NonTerminal( 44 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_type : class_or_interface_type 
    {
        $$= new NonTerminal( 62 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_type : class_or_interface_type 
    {
        $$= new NonTerminal( 37 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

array_type : primitive_type dims 
    {
        $$= new NonTerminal( 68 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

array_type : name dims 
    {
        $$= new NonTerminal( 68 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

name : simple_name 
    {
        $$= new NonTerminal( 26 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

name : qualified_name 
    {
        $$= new NonTerminal( 26 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

simple_name : identifier 
    {
        $$= new NonTerminal( 94 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

qualified_name : name DOT_TK identifier 
    {
        $$= new NonTerminal( 109 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

identifier : ID_TK 
    {
        $$= new NonTerminal( 31 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compilation_unit : 
    {
        $$= new NonTerminal( 20 );

    }
    ;

compilation_unit : package_declaration 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compilation_unit : import_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compilation_unit : type_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compilation_unit : package_declaration import_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

compilation_unit : package_declaration type_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

compilation_unit : import_declarations type_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

compilation_unit : package_declaration import_declarations type_declarations 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

import_declarations : import_declaration 
    {
        $$= new NonTerminal( 107 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

import_declarations : import_declarations import_declaration 
    {
        $$= new NonTerminal( 107 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

type_declarations : type_declaration 
    {
        $$= new NonTerminal( 47 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type_declarations : type_declarations type_declaration 
    {
        $$= new NonTerminal( 47 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

package_declaration : PACKAGE_TK name SC_TK 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

package_declaration : PACKAGE_TK error 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

package_declaration : PACKAGE_TK name error 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

import_declaration : single_type_import_declaration 
    {
        $$= new NonTerminal( 6 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

import_declaration : type_import_on_demand_declaration 
    {
        $$= new NonTerminal( 6 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

single_type_import_declaration : IMPORT_TK name SC_TK 
    {
        $$= new NonTerminal( 69 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

single_type_import_declaration : IMPORT_TK error 
    {
        $$= new NonTerminal( 69 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

single_type_import_declaration : IMPORT_TK name error 
    {
        $$= new NonTerminal( 69 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

type_import_on_demand_declaration : IMPORT_TK name DOT_TK MULT_TK SC_TK 
    {
        $$= new NonTerminal( 115 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

type_import_on_demand_declaration : IMPORT_TK name DOT_TK error 
    {
        $$= new NonTerminal( 115 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

type_import_on_demand_declaration : IMPORT_TK name DOT_TK MULT_TK error 
    {
        $$= new NonTerminal( 115 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

type_declaration : class_declaration 
    {
        $$= new NonTerminal( 128 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type_declaration : interface_declaration 
    {
        $$= new NonTerminal( 128 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type_declaration : empty_statement 
    {
        $$= new NonTerminal( 128 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

type_declaration : error 
    {
        $$= new NonTerminal( 128 );

    }
    ;

modifiers : modifier 
    {
        $$= new NonTerminal( 116 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifiers : modifiers modifier 
    {
        $$= new NonTerminal( 116 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

modifier : PUBLIC_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : PRIVATE_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : PROTECTED_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : STATIC_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : FINAL_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : SYNCHRONIZED_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : VOLATILE_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : TRANSIENT_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : NATIVE_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : PAD_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : ABSTRACT_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : STRICT_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : CONST_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

modifier : MODIFIER_TK 
    {
        $$= new NonTerminal( 125 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_declaration : modifiers CLASS_TK identifier super interfaces class_body 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

class_declaration : CLASS_TK identifier super interfaces class_body 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

class_declaration : modifiers CLASS_TK error 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_declaration : CLASS_TK error 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_declaration : CLASS_TK identifier error 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_declaration : modifiers CLASS_TK identifier error 
    {
        $$= new NonTerminal( 150 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

super : 
    {
        $$= new NonTerminal( 70 );

    }
    ;

super : EXTENDS_TK class_type 
    {
        $$= new NonTerminal( 70 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

super : EXTENDS_TK class_type error 
    {
        $$= new NonTerminal( 70 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

super : EXTENDS_TK error 
    {
        $$= new NonTerminal( 70 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interfaces : 
    {
        $$= new NonTerminal( 63 );

    }
    ;

interfaces : IMPLEMENTS_TK interface_type_list 
    {
        $$= new NonTerminal( 63 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interfaces : IMPLEMENTS_TK error 
    {
        $$= new NonTerminal( 63 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_type_list : interface_type 
    {
        $$= new NonTerminal( 58 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_type_list : interface_type_list C_TK interface_type 
    {
        $$= new NonTerminal( 58 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

interface_type_list : interface_type_list C_TK error 
    {
        $$= new NonTerminal( 58 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_body : OCB_TK CCB_TK 
    {
        $$= new NonTerminal( 111 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_body : OCB_TK class_body_declarations CCB_TK 
    {
        $$= new NonTerminal( 111 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

class_body_declarations : class_body_declaration 
    {
        $$= new NonTerminal( 126 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_body_declarations : class_body_declarations class_body_declaration 
    {
        $$= new NonTerminal( 126 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_body_declaration : class_member_declaration 
    {
        $$= new NonTerminal( 101 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_body_declaration : static_initializer 
    {
        $$= new NonTerminal( 101 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_body_declaration : constructor_declaration 
    {
        $$= new NonTerminal( 101 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_body_declaration : block 
    {
        $$= new NonTerminal( 101 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_member_declaration : field_declaration 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_member_declaration : method_declaration 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_member_declaration : class_declaration 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_member_declaration : interface_declaration 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_member_declaration : empty_statement 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

field_declaration : type variable_declarators SC_TK 
    {
        $$= new NonTerminal( 122 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

field_declaration : modifiers type variable_declarators SC_TK 
    {
        $$= new NonTerminal( 122 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

variable_declarators : variable_declarator 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_declarators : variable_declarators C_TK variable_declarator 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

variable_declarators : variable_declarators C_TK error 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

variable_declarator : variable_declarator_id 
    {
        $$= new NonTerminal( 54 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_declarator : variable_declarator_id ASSIGN_TK variable_initializer 
    {
        $$= new NonTerminal( 54 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

variable_declarator : variable_declarator_id ASSIGN_TK error 
    {
        $$= new NonTerminal( 54 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

variable_declarator : variable_declarator_id ASSIGN_TK variable_initializer error 
    {
        $$= new NonTerminal( 54 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

variable_declarator_id : identifier 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_declarator_id : variable_declarator_id OSB_TK CSB_TK 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

variable_declarator_id : identifier error 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_declarator_id : variable_declarator_id OSB_TK error 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

variable_declarator_id : variable_declarator_id CSB_TK error 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

variable_initializer : expression 
    {
        $$= new NonTerminal( 102 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_initializer : array_initializer 
    {
        $$= new NonTerminal( 102 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_declaration : method_header method_body 
    {
        $$= new NonTerminal( 95 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

method_declaration : method_header error 
    {
        $$= new NonTerminal( 95 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_header : type method_declarator throws 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

method_header : VOID_TK method_declarator throws 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

method_header : modifiers type method_declarator throws 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

method_header : modifiers VOID_TK method_declarator throws 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

method_header : type error 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_header : modifiers type error 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

method_header : VOID_TK error 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_header : modifiers VOID_TK error 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

method_header : modifiers error 
    {
        $$= new NonTerminal( 118 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_declarator : identifier OP_TK CP_TK 
    {
        $$= new NonTerminal( 154 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

method_declarator : identifier OP_TK formal_parameter_list CP_TK 
    {
        $$= new NonTerminal( 154 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

method_declarator : method_declarator OSB_TK CSB_TK 
    {
        $$= new NonTerminal( 154 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

method_declarator : identifier OP_TK error 
    {
        $$= new NonTerminal( 154 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

method_declarator : method_declarator OSB_TK error 
    {
        $$= new NonTerminal( 154 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

formal_parameter_list : formal_parameter 
    {
        $$= new NonTerminal( 16 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

formal_parameter_list : formal_parameter_list C_TK formal_parameter 
    {
        $$= new NonTerminal( 16 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

formal_parameter_list : formal_parameter_list C_TK error 
    {
        $$= new NonTerminal( 16 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

formal_parameter : type variable_declarator_id 
    {
        $$= new NonTerminal( 110 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

formal_parameter : final type variable_declarator_id 
    {
        $$= new NonTerminal( 110 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

formal_parameter : type error 
    {
        $$= new NonTerminal( 110 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

formal_parameter : final type error 
    {
        $$= new NonTerminal( 110 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

final : modifiers 
    {
        $$= new NonTerminal( 106 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

throws : 
    {
        $$= new NonTerminal( 81 );

    }
    ;

throws : THROWS_TK class_type_list 
    {
        $$= new NonTerminal( 81 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

throws : THROWS_TK error 
    {
        $$= new NonTerminal( 81 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_type_list : class_type 
    {
        $$= new NonTerminal( 142 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_type_list : class_type_list C_TK class_type 
    {
        $$= new NonTerminal( 142 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

class_type_list : class_type_list C_TK error 
    {
        $$= new NonTerminal( 142 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

method_body : block 
    {
        $$= new NonTerminal( 72 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_body : SC_TK 
    {
        $$= new NonTerminal( 72 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_initializer : static block 
    {
        $$= new NonTerminal( 24 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

static : modifiers 
    {
        $$= new NonTerminal( 41 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

constructor_declaration : constructor_header constructor_body 
    {
        $$= new NonTerminal( 155 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

constructor_header : constructor_declarator throws 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

constructor_header : modifiers constructor_declarator throws 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

constructor_declarator : simple_name OP_TK CP_TK 
    {
        $$= new NonTerminal( 33 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

constructor_declarator : simple_name OP_TK formal_parameter_list CP_TK 
    {
        $$= new NonTerminal( 33 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

constructor_body : block_begin constructor_block_end 
    {
        $$= new NonTerminal( 143 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

constructor_body : block_begin explicit_constructor_invocation constructor_block_end 
    {
        $$= new NonTerminal( 143 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

constructor_body : block_begin block_statements constructor_block_end 
    {
        $$= new NonTerminal( 143 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

constructor_body : block_begin explicit_constructor_invocation block_statements constructor_block_end 
    {
        $$= new NonTerminal( 143 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

constructor_block_end : block_end 
    {
        $$= new NonTerminal( 32 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

explicit_constructor_invocation : this_or_super OP_TK CP_TK SC_TK 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

explicit_constructor_invocation : this_or_super OP_TK argument_list CP_TK SC_TK 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

explicit_constructor_invocation : name DOT_TK SUPER_TK OP_TK argument_list CP_TK SC_TK 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

explicit_constructor_invocation : name DOT_TK SUPER_TK OP_TK CP_TK SC_TK 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

this_or_super : THIS_TK 
    {
        $$= new NonTerminal( 148 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

this_or_super : SUPER_TK 
    {
        $$= new NonTerminal( 148 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_declaration : INTERFACE_TK identifier interface_body 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

interface_declaration : modifiers INTERFACE_TK identifier interface_body 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

interface_declaration : INTERFACE_TK identifier extends_interfaces interface_body 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

interface_declaration : modifiers INTERFACE_TK identifier extends_interfaces interface_body 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

interface_declaration : INTERFACE_TK identifier error 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_declaration : modifiers INTERFACE_TK identifier error 
    {
        $$= new NonTerminal( 35 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

extends_interfaces : EXTENDS_TK interface_type 
    {
        $$= new NonTerminal( 92 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

extends_interfaces : extends_interfaces C_TK interface_type 
    {
        $$= new NonTerminal( 92 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

extends_interfaces : EXTENDS_TK error 
    {
        $$= new NonTerminal( 92 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

extends_interfaces : extends_interfaces C_TK error 
    {
        $$= new NonTerminal( 92 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_body : OCB_TK CCB_TK 
    {
        $$= new NonTerminal( 75 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_body : OCB_TK interface_member_declarations CCB_TK 
    {
        $$= new NonTerminal( 75 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

interface_member_declarations : interface_member_declaration 
    {
        $$= new NonTerminal( 129 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_member_declarations : interface_member_declarations interface_member_declaration 
    {
        $$= new NonTerminal( 129 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_member_declaration : constant_declaration 
    {
        $$= new NonTerminal( 48 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_member_declaration : abstract_method_declaration 
    {
        $$= new NonTerminal( 48 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_member_declaration : class_declaration 
    {
        $$= new NonTerminal( 48 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_member_declaration : interface_declaration 
    {
        $$= new NonTerminal( 48 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

constant_declaration : field_declaration 
    {
        $$= new NonTerminal( 139 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

abstract_method_declaration : method_header SC_TK 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

abstract_method_declaration : method_header error 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

array_initializer : OCB_TK CCB_TK 
    {
        $$= new NonTerminal( 147 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

array_initializer : OCB_TK C_TK CCB_TK 
    {
        $$= new NonTerminal( 147 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_initializer : OCB_TK variable_initializers CCB_TK 
    {
        $$= new NonTerminal( 147 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_initializer : OCB_TK variable_initializers C_TK CCB_TK 
    {
        $$= new NonTerminal( 147 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

variable_initializers : variable_initializer 
    {
        $$= new NonTerminal( 141 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_initializers : variable_initializers C_TK variable_initializer 
    {
        $$= new NonTerminal( 141 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

variable_initializers : variable_initializers C_TK error 
    {
        $$= new NonTerminal( 141 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

block : block_begin block_end 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

block : block_begin block_statements block_end 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

block_begin : OCB_TK 
    {
        $$= new NonTerminal( 5 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

block_end : CCB_TK 
    {
        $$= new NonTerminal( 19 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

block_statements : block_statement 
    {
        $$= new NonTerminal( 140 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

block_statements : block_statements block_statement 
    {
        $$= new NonTerminal( 140 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

block_statement : local_variable_declaration_statement 
    {
        $$= new NonTerminal( 97 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

block_statement : statement 
    {
        $$= new NonTerminal( 97 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

block_statement : class_declaration 
    {
        $$= new NonTerminal( 97 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

local_variable_declaration_statement : local_variable_declaration SC_TK 
    {
        $$= new NonTerminal( 18 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

local_variable_declaration : type variable_declarators 
    {
        $$= new NonTerminal( 124 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

local_variable_declaration : final type variable_declarators 
    {
        $$= new NonTerminal( 124 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

statement : statement_without_trailing_substatement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : labeled_statement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : if_then_statement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : if_then_else_statement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : while_statement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : for_statement 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_nsi : statement_without_trailing_substatement 
    {
        $$= new NonTerminal( 149 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_nsi : labeled_statement_nsi 
    {
        $$= new NonTerminal( 149 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_nsi : if_then_else_statement_nsi 
    {
        $$= new NonTerminal( 149 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_nsi : while_statement_nsi 
    {
        $$= new NonTerminal( 149 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_nsi : for_statement_nsi 
    {
        $$= new NonTerminal( 149 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : block 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : empty_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : expression_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : switch_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : do_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : break_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : continue_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : return_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : synchronized_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : throw_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : try_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_without_trailing_substatement : assert_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

empty_statement : SC_TK 
    {
        $$= new NonTerminal( 120 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

label_decl : identifier REL_CL_TK 
    {
        $$= new NonTerminal( 15 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

labeled_statement : label_decl statement 
    {
        $$= new NonTerminal( 42 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

labeled_statement : identifier error 
    {
        $$= new NonTerminal( 42 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

labeled_statement_nsi : label_decl statement_nsi 
    {
        $$= new NonTerminal( 121 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : statement_expression SC_TK 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : error SC_TK 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : error OCB_TK 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : error CCB_TK 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : this_or_super OP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expression_statement : this_or_super OP_TK CP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

expression_statement : this_or_super OP_TK argument_list error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

expression_statement : this_or_super OP_TK argument_list CP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

expression_statement : name DOT_TK SUPER_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

expression_statement : name DOT_TK SUPER_TK OP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

expression_statement : name DOT_TK SUPER_TK OP_TK argument_list error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

expression_statement : name DOT_TK SUPER_TK OP_TK argument_list CP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

expression_statement : name DOT_TK SUPER_TK OP_TK CP_TK error 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

statement_expression : assignment 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : pre_increment_expression 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : pre_decrement_expression 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : post_increment_expression 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : post_decrement_expression 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : method_invocation 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression : class_instance_creation_expression 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

if_then_statement : IF_TK OP_TK expression CP_TK statement 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

if_then_statement : IF_TK error 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

if_then_statement : IF_TK OP_TK error 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

if_then_statement : IF_TK OP_TK expression error 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

if_then_else_statement : IF_TK OP_TK expression CP_TK statement_nsi ELSE_TK statement 
    {
        $$= new NonTerminal( 83 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

if_then_else_statement_nsi : IF_TK OP_TK expression CP_TK statement_nsi ELSE_TK statement_nsi 
    {
        $$= new NonTerminal( 74 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

switch_statement : switch_expression switch_block 
    {
        $$= new NonTerminal( 45 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_expression : SWITCH_TK OP_TK expression CP_TK 
    {
        $$= new NonTerminal( 34 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

switch_expression : SWITCH_TK error 
    {
        $$= new NonTerminal( 34 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

switch_expression : SWITCH_TK OP_TK error 
    {
        $$= new NonTerminal( 34 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_expression : SWITCH_TK OP_TK expression CP_TK error 
    {
        $$= new NonTerminal( 34 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

switch_block : OCB_TK CCB_TK 
    {
        $$= new NonTerminal( 152 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_block : OCB_TK switch_labels CCB_TK 
    {
        $$= new NonTerminal( 152 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

switch_block : OCB_TK switch_block_statement_groups CCB_TK 
    {
        $$= new NonTerminal( 152 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

switch_block : OCB_TK switch_block_statement_groups switch_labels CCB_TK 
    {
        $$= new NonTerminal( 152 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

switch_block_statement_groups : switch_block_statement_group 
    {
        $$= new NonTerminal( 8 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

switch_block_statement_groups : switch_block_statement_groups switch_block_statement_group 
    {
        $$= new NonTerminal( 8 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_block_statement_group : switch_labels block_statements 
    {
        $$= new NonTerminal( 146 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_labels : switch_label 
    {
        $$= new NonTerminal( 53 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

switch_labels : switch_labels switch_label 
    {
        $$= new NonTerminal( 53 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_label : CASE_TK constant_expression REL_CL_TK 
    {
        $$= new NonTerminal( 113 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

switch_label : DEFAULT_TK REL_CL_TK 
    {
        $$= new NonTerminal( 113 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_label : CASE_TK error 
    {
        $$= new NonTerminal( 113 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

switch_label : CASE_TK constant_expression error 
    {
        $$= new NonTerminal( 113 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

switch_label : DEFAULT_TK error 
    {
        $$= new NonTerminal( 113 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

while_expression : WHILE_TK OP_TK expression CP_TK 
    {
        $$= new NonTerminal( 151 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

while_statement : while_expression statement 
    {
        $$= new NonTerminal( 12 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

while_statement : WHILE_TK error 
    {
        $$= new NonTerminal( 12 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

while_statement : WHILE_TK OP_TK error 
    {
        $$= new NonTerminal( 12 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

while_statement : WHILE_TK OP_TK expression error 
    {
        $$= new NonTerminal( 12 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

while_statement_nsi : while_expression statement_nsi 
    {
        $$= new NonTerminal( 134 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

do_statement_begin : DO_TK 
    {
        $$= new NonTerminal( 79 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

do_statement : do_statement_begin statement WHILE_TK OP_TK expression CP_TK SC_TK 
    {
        $$= new NonTerminal( 138 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

for_statement : for_begin SC_TK expression SC_TK for_update CP_TK statement 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

for_statement : for_begin SC_TK SC_TK for_update CP_TK statement 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

for_statement : for_begin SC_TK error 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

for_statement : for_begin SC_TK expression SC_TK error 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

for_statement : for_begin SC_TK SC_TK error 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

for_statement_nsi : for_begin SC_TK expression SC_TK for_update CP_TK statement_nsi 
    {
        $$= new NonTerminal( 11 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

for_statement_nsi : for_begin SC_TK SC_TK for_update CP_TK statement_nsi 
    {
        $$= new NonTerminal( 11 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

for_header : FOR_TK OP_TK 
    {
        $$= new NonTerminal( 119 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

for_header : FOR_TK error 
    {
        $$= new NonTerminal( 119 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_header : FOR_TK OP_TK error 
    {
        $$= new NonTerminal( 119 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

for_begin : for_header for_init 
    {
        $$= new NonTerminal( 93 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

for_init : 
    {
        $$= new NonTerminal( 88 );

    }
    ;

for_init : statement_expression_list 
    {
        $$= new NonTerminal( 88 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_init : local_variable_declaration 
    {
        $$= new NonTerminal( 88 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_init : statement_expression_list error 
    {
        $$= new NonTerminal( 88 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_update : 
    {
        $$= new NonTerminal( 66 );

    }
    ;

for_update : statement_expression_list 
    {
        $$= new NonTerminal( 66 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression_list : statement_expression 
    {
        $$= new NonTerminal( 67 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement_expression_list : statement_expression_list C_TK statement_expression 
    {
        $$= new NonTerminal( 67 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

statement_expression_list : statement_expression_list C_TK error 
    {
        $$= new NonTerminal( 67 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

break_statement : BREAK_TK SC_TK 
    {
        $$= new NonTerminal( 39 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

break_statement : BREAK_TK identifier SC_TK 
    {
        $$= new NonTerminal( 39 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

break_statement : BREAK_TK error 
    {
        $$= new NonTerminal( 39 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

break_statement : BREAK_TK identifier error 
    {
        $$= new NonTerminal( 39 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

continue_statement : CONTINUE_TK SC_TK 
    {
        $$= new NonTerminal( 55 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

continue_statement : CONTINUE_TK identifier SC_TK 
    {
        $$= new NonTerminal( 55 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

continue_statement : CONTINUE_TK error 
    {
        $$= new NonTerminal( 55 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

continue_statement : CONTINUE_TK identifier error 
    {
        $$= new NonTerminal( 55 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

return_statement : RETURN_TK SC_TK 
    {
        $$= new NonTerminal( 137 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

return_statement : RETURN_TK expression SC_TK 
    {
        $$= new NonTerminal( 137 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

return_statement : RETURN_TK error 
    {
        $$= new NonTerminal( 137 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

return_statement : RETURN_TK expression error 
    {
        $$= new NonTerminal( 137 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

throw_statement : THROW_TK expression SC_TK 
    {
        $$= new NonTerminal( 4 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

throw_statement : THROW_TK error 
    {
        $$= new NonTerminal( 4 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

throw_statement : THROW_TK expression error 
    {
        $$= new NonTerminal( 4 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

assert_statement : ASSERT_TK expression REL_CL_TK expression SC_TK 
    {
        $$= new NonTerminal( 9 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

assert_statement : ASSERT_TK expression SC_TK 
    {
        $$= new NonTerminal( 9 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

assert_statement : ASSERT_TK error 
    {
        $$= new NonTerminal( 9 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assert_statement : ASSERT_TK expression error 
    {
        $$= new NonTerminal( 9 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

synchronized_statement : synchronized OP_TK expression CP_TK block 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

synchronized_statement : synchronized OP_TK expression CP_TK error 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

synchronized_statement : synchronized error 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

synchronized_statement : synchronized OP_TK error CP_TK 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

synchronized_statement : synchronized OP_TK error 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

synchronized : modifiers 
    {
        $$= new NonTerminal( 127 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

try_statement : TRY_TK block catches 
    {
        $$= new NonTerminal( 130 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

try_statement : TRY_TK block finally 
    {
        $$= new NonTerminal( 130 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

try_statement : TRY_TK block catches finally 
    {
        $$= new NonTerminal( 130 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

try_statement : TRY_TK error 
    {
        $$= new NonTerminal( 130 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

catches : catch_clause 
    {
        $$= new NonTerminal( 25 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

catches : catches catch_clause 
    {
        $$= new NonTerminal( 25 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

catch_clause : catch_clause_parameter block 
    {
        $$= new NonTerminal( 64 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

catch_clause_parameter : CATCH_TK OP_TK formal_parameter CP_TK 
    {
        $$= new NonTerminal( 112 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

catch_clause_parameter : CATCH_TK error 
    {
        $$= new NonTerminal( 112 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

catch_clause_parameter : CATCH_TK OP_TK error 
    {
        $$= new NonTerminal( 112 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

catch_clause_parameter : CATCH_TK OP_TK error CP_TK 
    {
        $$= new NonTerminal( 112 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

finally : FINALLY_TK block 
    {
        $$= new NonTerminal( 96 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

finally : FINALLY_TK error 
    {
        $$= new NonTerminal( 96 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary : primary_no_new_array 
    {
        $$= new NonTerminal( 87 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary : array_creation_expression 
    {
        $$= new NonTerminal( 87 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : literal 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : THIS_TK 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : OP_TK expression CP_TK 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

primary_no_new_array : class_instance_creation_expression 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : field_access 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : method_invocation 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : array_access 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : type_literals 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

primary_no_new_array : name DOT_TK THIS_TK 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

primary_no_new_array : OP_TK expression error 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

primary_no_new_array : name DOT_TK error 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

primary_no_new_array : primitive_type DOT_TK error 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

primary_no_new_array : VOID_TK DOT_TK error 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

type_literals : name DOT_TK CLASS_TK 
    {
        $$= new NonTerminal( 117 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

type_literals : array_type DOT_TK CLASS_TK 
    {
        $$= new NonTerminal( 117 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

type_literals : primitive_type DOT_TK CLASS_TK 
    {
        $$= new NonTerminal( 117 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

type_literals : VOID_TK DOT_TK CLASS_TK 
    {
        $$= new NonTerminal( 117 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK class_type OP_TK argument_list CP_TK 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK class_type OP_TK CP_TK 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

class_instance_creation_expression : anonymous_class_creation 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new identifier OP_TK CP_TK 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new identifier OP_TK CP_TK class_body 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new identifier OP_TK argument_list CP_TK 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new identifier OP_TK argument_list CP_TK class_body 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK error SC_TK 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK class_type error 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK class_type OP_TK error 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

class_instance_creation_expression : NEW_TK class_type OP_TK argument_list error 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new error 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_instance_creation_expression : something_dot_new identifier error 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

anonymous_class_creation : NEW_TK class_type OP_TK argument_list CP_TK class_body 
    {
        $$= new NonTerminal( 49 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

anonymous_class_creation : NEW_TK class_type OP_TK CP_TK class_body 
    {
        $$= new NonTerminal( 49 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

something_dot_new : name DOT_TK NEW_TK 
    {
        $$= new NonTerminal( 27 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

something_dot_new : primary DOT_TK NEW_TK 
    {
        $$= new NonTerminal( 27 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

argument_list : expression 
    {
        $$= new NonTerminal( 56 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

argument_list : argument_list C_TK expression 
    {
        $$= new NonTerminal( 56 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

argument_list : argument_list C_TK error 
    {
        $$= new NonTerminal( 56 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

array_creation_expression : NEW_TK primitive_type dim_exprs 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_creation_expression : NEW_TK class_or_interface_type dim_exprs 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_creation_expression : NEW_TK primitive_type dim_exprs dims 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_creation_expression : NEW_TK class_or_interface_type dim_exprs dims 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_creation_expression : NEW_TK class_or_interface_type dims array_initializer 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_creation_expression : NEW_TK primitive_type dims array_initializer 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_creation_expression : NEW_TK error CSB_TK 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_creation_expression : NEW_TK error OSB_TK 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

dim_exprs : dim_expr 
    {
        $$= new NonTerminal( 43 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

dim_exprs : dim_exprs dim_expr 
    {
        $$= new NonTerminal( 43 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

dim_expr : OSB_TK expression CSB_TK 
    {
        $$= new NonTerminal( 133 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

dim_expr : OSB_TK expression error 
    {
        $$= new NonTerminal( 133 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

dim_expr : OSB_TK error 
    {
        $$= new NonTerminal( 133 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

dims : OSB_TK CSB_TK 
    {
        $$= new NonTerminal( 144 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

dims : dims OSB_TK CSB_TK 
    {
        $$= new NonTerminal( 144 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

dims : dims OSB_TK error 
    {
        $$= new NonTerminal( 144 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

field_access : primary DOT_TK identifier 
    {
        $$= new NonTerminal( 61 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

field_access : SUPER_TK DOT_TK identifier 
    {
        $$= new NonTerminal( 61 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

field_access : SUPER_TK error 
    {
        $$= new NonTerminal( 61 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_invocation : name OP_TK CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

method_invocation : name OP_TK argument_list CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

method_invocation : primary DOT_TK identifier OP_TK CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

method_invocation : primary DOT_TK identifier OP_TK argument_list CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

method_invocation : SUPER_TK DOT_TK identifier OP_TK CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

method_invocation : SUPER_TK DOT_TK identifier OP_TK argument_list CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

method_invocation : SUPER_TK DOT_TK error CP_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

method_invocation : SUPER_TK DOT_TK error DOT_TK 
    {
        $$= new NonTerminal( 104 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_access : name OSB_TK expression CSB_TK 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_access : primary_no_new_array OSB_TK expression CSB_TK 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

array_access : name OSB_TK error 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

array_access : name OSB_TK expression error 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

array_access : primary_no_new_array OSB_TK error 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

array_access : primary_no_new_array OSB_TK expression error 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

postfix_expression : primary 
    {
        $$= new NonTerminal( 136 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

postfix_expression : name 
    {
        $$= new NonTerminal( 136 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

postfix_expression : post_increment_expression 
    {
        $$= new NonTerminal( 136 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

postfix_expression : post_decrement_expression 
    {
        $$= new NonTerminal( 136 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

post_increment_expression : postfix_expression INCR_TK 
    {
        $$= new NonTerminal( 2 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

post_decrement_expression : postfix_expression DECR_TK 
    {
        $$= new NonTerminal( 7 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

trap_overflow_corner_case : pre_increment_expression 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

trap_overflow_corner_case : pre_decrement_expression 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

trap_overflow_corner_case : PLUS_TK unary_expression 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

trap_overflow_corner_case : unary_expression_not_plus_minus 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

trap_overflow_corner_case : PLUS_TK error 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression : trap_overflow_corner_case 
    {
        $$= new NonTerminal( 36 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression : MINUS_TK trap_overflow_corner_case 
    {
        $$= new NonTerminal( 36 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unary_expression : MINUS_TK error 
    {
        $$= new NonTerminal( 36 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

pre_increment_expression : INCR_TK unary_expression 
    {
        $$= new NonTerminal( 77 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

pre_increment_expression : INCR_TK error 
    {
        $$= new NonTerminal( 77 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

pre_decrement_expression : DECR_TK unary_expression 
    {
        $$= new NonTerminal( 30 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

pre_decrement_expression : DECR_TK error 
    {
        $$= new NonTerminal( 30 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression_not_plus_minus : postfix_expression 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression_not_plus_minus : NOT_TK unary_expression 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unary_expression_not_plus_minus : NEG_TK unary_expression 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unary_expression_not_plus_minus : cast_expression 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression_not_plus_minus : NOT_TK error 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unary_expression_not_plus_minus : NEG_TK error 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

cast_expression : OP_TK primitive_type dims CP_TK unary_expression 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

cast_expression : OP_TK primitive_type CP_TK unary_expression 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

cast_expression : OP_TK expression CP_TK unary_expression_not_plus_minus 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

cast_expression : OP_TK name dims CP_TK unary_expression_not_plus_minus 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

cast_expression : OP_TK primitive_type OSB_TK error 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

cast_expression : OP_TK error 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

cast_expression : OP_TK primitive_type dims CP_TK error 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

cast_expression : OP_TK primitive_type CP_TK error 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

cast_expression : OP_TK name dims CP_TK error 
    {
        $$= new NonTerminal( 135 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

multiplicative_expression : unary_expression 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression MULT_TK unary_expression 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression DIV_TK unary_expression 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression REM_TK unary_expression 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression MULT_TK error 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression DIV_TK error 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

multiplicative_expression : multiplicative_expression REM_TK error 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

additive_expression : multiplicative_expression 
    {
        $$= new NonTerminal( 145 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

additive_expression : additive_expression PLUS_TK multiplicative_expression 
    {
        $$= new NonTerminal( 145 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

additive_expression : additive_expression MINUS_TK multiplicative_expression 
    {
        $$= new NonTerminal( 145 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

additive_expression : additive_expression PLUS_TK error 
    {
        $$= new NonTerminal( 145 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

additive_expression : additive_expression MINUS_TK error 
    {
        $$= new NonTerminal( 145 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

shift_expression : additive_expression 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

shift_expression : shift_expression LS_TK additive_expression 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

shift_expression : shift_expression SRS_TK additive_expression 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

shift_expression : shift_expression ZRS_TK additive_expression 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

shift_expression : shift_expression LS_TK error 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

shift_expression : shift_expression SRS_TK error 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

shift_expression : shift_expression ZRS_TK error 
    {
        $$= new NonTerminal( 98 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

relational_expression : shift_expression 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

relational_expression : relational_expression LT_TK shift_expression 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

relational_expression : relational_expression GT_TK shift_expression 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

relational_expression : relational_expression LTE_TK shift_expression 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

relational_expression : relational_expression GTE_TK shift_expression 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

relational_expression : relational_expression INSTANCEOF_TK reference_type 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

relational_expression : relational_expression LT_TK error 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

relational_expression : relational_expression GT_TK error 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

relational_expression : relational_expression LTE_TK error 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

relational_expression : relational_expression GTE_TK error 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

relational_expression : relational_expression INSTANCEOF_TK error 
    {
        $$= new NonTerminal( 108 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

equality_expression : relational_expression 
    {
        $$= new NonTerminal( 114 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

equality_expression : equality_expression EQ_TK relational_expression 
    {
        $$= new NonTerminal( 114 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

equality_expression : equality_expression NEQ_TK relational_expression 
    {
        $$= new NonTerminal( 114 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

equality_expression : equality_expression EQ_TK error 
    {
        $$= new NonTerminal( 114 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

equality_expression : equality_expression NEQ_TK error 
    {
        $$= new NonTerminal( 114 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

and_expression : equality_expression 
    {
        $$= new NonTerminal( 131 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

and_expression : and_expression AND_TK equality_expression 
    {
        $$= new NonTerminal( 131 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

and_expression : and_expression AND_TK error 
    {
        $$= new NonTerminal( 131 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

exclusive_or_expression : and_expression 
    {
        $$= new NonTerminal( 84 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

exclusive_or_expression : exclusive_or_expression XOR_TK and_expression 
    {
        $$= new NonTerminal( 84 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

exclusive_or_expression : exclusive_or_expression XOR_TK error 
    {
        $$= new NonTerminal( 84 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

inclusive_or_expression : exclusive_or_expression 
    {
        $$= new NonTerminal( 10 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

inclusive_or_expression : inclusive_or_expression OR_TK exclusive_or_expression 
    {
        $$= new NonTerminal( 10 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

inclusive_or_expression : inclusive_or_expression OR_TK error 
    {
        $$= new NonTerminal( 10 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

conditional_and_expression : inclusive_or_expression 
    {
        $$= new NonTerminal( 76 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

conditional_and_expression : conditional_and_expression BOOL_AND_TK inclusive_or_expression 
    {
        $$= new NonTerminal( 76 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

conditional_and_expression : conditional_and_expression BOOL_AND_TK error 
    {
        $$= new NonTerminal( 76 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

conditional_or_expression : conditional_and_expression 
    {
        $$= new NonTerminal( 59 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

conditional_or_expression : conditional_or_expression BOOL_OR_TK conditional_and_expression 
    {
        $$= new NonTerminal( 59 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

conditional_or_expression : conditional_or_expression BOOL_OR_TK error 
    {
        $$= new NonTerminal( 59 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

conditional_expression : conditional_or_expression 
    {
        $$= new NonTerminal( 153 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

conditional_expression : conditional_or_expression REL_QM_TK expression REL_CL_TK conditional_expression 
    {
        $$= new NonTerminal( 153 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

conditional_expression : conditional_or_expression REL_QM_TK REL_CL_TK error 
    {
        $$= new NonTerminal( 153 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

conditional_expression : conditional_or_expression REL_QM_TK error 
    {
        $$= new NonTerminal( 153 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

conditional_expression : conditional_or_expression REL_QM_TK expression REL_CL_TK error 
    {
        $$= new NonTerminal( 153 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

assignment_expression : conditional_expression 
    {
        $$= new NonTerminal( 46 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment_expression : assignment 
    {
        $$= new NonTerminal( 46 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment : left_hand_side assignment_operator assignment_expression 
    {
        $$= new NonTerminal( 57 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

        $2->nextSibbling= $3;

        $$->addChild($3);

        $3->parent= $$;

    }
    ;

assignment : left_hand_side assignment_operator error 
    {
        $$= new NonTerminal( 57 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

left_hand_side : name 
    {
        $$= new NonTerminal( 38 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

left_hand_side : field_access 
    {
        $$= new NonTerminal( 38 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

left_hand_side : array_access 
    {
        $$= new NonTerminal( 38 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment_operator : assign_any 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment_operator : ASSIGN_TK 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : PLUS_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : MINUS_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : MULT_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : DIV_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : REM_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : LS_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : SRS_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : ZRS_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : AND_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : XOR_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assign_any : OR_ASSIGN_TK 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expression : assignment_expression 
    {
        $$= new NonTerminal( 80 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

constant_expression : expression 
    {
        $$= new NonTerminal( 123 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;



%%


#include <stdio.h>

extern char yytext[];
extern int column;
extern int line;

void yyerror( char *s)
{
fflush(stdout);
fprintf(stderr,"%s: %d.%d\n",s,line,column);
}


