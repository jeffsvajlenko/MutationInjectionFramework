
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


%type <t> foreach_variable
%type <t> scalar
%type <t> w_variable
%type <t> static_scalar
%type <t> unset_variables
%type <t> while_statement
%type <t> reference_variable
%type <t> for_statement
%type <t> additional_catch
%type <t> exit_expr
%type <t> ctor_arguments
%type <t> encaps_var
%type <t> parameter_list
%type <t> assignment_list_element
%type <t> class_variable_declaration
%type <t> dynamic_class_name_variable_properties
%type <t> dynamic_class_name_variable_property
%type <t> inner_statement
%type <t> class_constant
%type <t> function_call
%type <t> member_modifier
%type <t> internal_functions_in_yacc
%type <t> declare_statement
%type <t> non_empty_static_array_pair_list
%type <t> class_name_reference
%type <t> else_single
%type <t> base_variable_with_function_calls
%type <t> inner_statement_list
%type <t> unticked_statement
%type <t> object_property
%type <t> implements_list
%type <t> static_var_list
%type <t> static_class_constant
%type <t> class_declaration_statement
%type <t> non_empty_parameter_list
%type <t> unticked_class_declaration_statement
%type <t> object_dim_list
%type <t> variable_modifiers
%type <t> interface_extends_list
%type <t> assignment_list
%type <t> non_empty_function_call_parameter_list
%type <t> interface_list
%type <t> top_statement
%type <t> encaps_var_offset
%type <t> non_empty_for_expr
%type <t> class_entry_type
%type <t> elseif_list
%type <t> class_statement
%type <t> case_list
%type <t> use_filename
%type <t> class_statement_list
%type <t> expr_without_variable
%type <t> rw_variable
%type <t> base_variable
%type <t> isset_variables
%type <t> start
%type <t> function_declaration_statement
%type <t> statement
%type <t> variable_name
%type <t> stmt_end
%type <t> non_empty_array_pair_list
%type <t> dim_offset
%type <t> unset_variable
%type <t> global_var
%type <t> is_reference
%type <t> top_statement_list
%type <t> interface_entry
%type <t> simple_indirect_reference
%type <t> array_pair_list
%type <t> new_else_single
%type <t> echo_expr_list
%type <t> expr
%type <t> compound_variable
%type <t> method_modifiers
%type <t> global_var_list
%type <t> switch_case_list
%type <t> class_constant_declaration
%type <t> unticked_function_declaration_statement
%type <t> encaps_list
%type <t> new_elseif_list
%type <t> extends_from
%type <t> possible_comma
%type <t> common_scalar
%type <t> variable_properties
%type <t> variable_without_objects
%type <t> foreach_statement
%type <t> fully_qualified_class_name
%type <t> non_empty_additional_catches
%type <t> static_member
%type <t> function_call_parameter_list
%type <t> non_empty_member_modifiers
%type <t> foreach_optional_arg
%type <t> additional_catches
%type <t> case_separator
%type <t> method_body
%type <t> declare_list
%type <t> r_variable
%type <t> variable
%type <t> method_or_not
%type <t> variable_property
%type <t> static_array_pair_list
%type <t> dynamic_class_name_reference
%type <t> for_expr
%type <t> optional_class_type
%type <t> T_DEC
%type <t> T_OBJECT_CAST
%type <t> T_ENDFOREACH
%type <t> '~'
%type <t> T_CONSTANT_ENCAPSED_STRING
%type <t> T_BAD_CHARACTER
%type <t> ';'
%type <t> T_DIV_EQUAL
%type <t> T_THROW
%type <t> T_INCLUDE_ONCE
%type <t> T_MUL_EQUAL
%type <t> T_ELSEIF
%type <t> T_LNUMBER
%type <t> T_STRING
%type <t> T_SL
%type <t> T_ABSTRACT
%type <t> T_IS_EQUAL
%type <t> T_IS_NOT_IDENTICAL
%type <t> T_CONTINUE
%type <t> '('
%type <t> T_ARRAY_CAST
%type <t> T_SR
%type <t> T_STRING_CAST
%type <t> '.'
%type <t> T_FILE
%type <t> ':'
%type <t> '\''
%type <t> T_REQUIRE_ONCE
%type <t> T_EXTENDS
%type <t> T_INCLUDE
%type <t> T_CLOSE_TAG
%type <t> T_CLASS
%type <t> T_FINAL
%type <t> T_CHARACTER
%type <t> T_ELSE
%type <t> T_CURLY_OPEN
%type <t> T_INT_CAST
%type <t> T_IS_GREATER_OR_EQUAL
%type <t> T_DNUMBER
%type <t> '+'
%type <t> T_LOGICAL_XOR
%type <t> T_EMPTY
%type <t> T_BOOLEAN_OR
%type <t> T_DECLARE
%type <t> T_BREAK
%type <t> '%'
%type <t> '`'
%type <t> T_ENCAPSED_AND_WHITESPACE
%type <t> '"'
%type <t> T_VAR
%type <t> '^'
%type <t> T_LOGICAL_AND
%type <t> T_ENDWHILE
%type <t> T_DEFAULT
%type <t> '*'
%type <t> T_DOUBLE_CAST
%type <t> '='
%type <t> T_ISSET
%type <t> T_DOLLAR_OPEN_CURLY_BRACES
%type <t> T_LOGICAL_OR
%type <t> T_HALT_COMPILER
%type <t> '{'
%type <t> T_AND_EQUAL
%type <t> '$'
%type <t> T_PUBLIC
%type <t> T_BOOLEAN_AND
%type <t> T_ECHO
%type <t> T_CONST
%type <t> T_SL_EQUAL
%type <t> T_CATCH
%type <t> '-'
%type <t> T_PLUS_EQUAL
%type <t> T_INSTANCEOF
%type <t> T_OR_EQUAL
%type <t> T_INLINE_HTML
%type <t> T_GLOBAL
%type <t> '<'
%type <t> T_INTERFACE
%type <t> T_INC
%type <t> T_CLASS_C
%type <t> T_UNSET_CAST
%type <t> T_ARRAY
%type <t> T_SR_EQUAL
%type <t> T_ENDDECLARE
%type <t> T_MINUS_EQUAL
%type <t> T_IS_IDENTICAL
%type <t> ','
%type <t> T_TRY
%type <t> T_IS_NOT_EQUAL
%type <t> T_CASE
%type <t> '?'
%type <t> '@'
%type <t> T_CONCAT_EQUAL
%type <t> '>'
%type <t> T_VARIABLE
%type <t> T_START_HEREDOC
%type <t> T_DO
%type <t> T_CLONE
%type <t> T_ENDIF
%type <t> T_RETURN
%type <t> '&'
%type <t> T_ENDFOR
%type <t> T_PROTECTED
%type <t> '/'
%type <t> T_PRINT
%type <t> '['
%type <t> T_EVAL
%type <t> T_XOR_EQUAL
%type <t> T_FOREACH
%type <t> T_NUM_STRING
%type <t> T_PRIVATE
%type <t> '}'
%type <t> T_SWITCH
%type <t> T_BOOL_CAST
%type <t> T_STRING_VARNAME
%type <t> T_UNSET
%type <t> T_ENDSWITCH
%type <t> T_FOR
%type <t> '!'
%type <t> ']'
%type <t> T_EXIT
%type <t> T_DOUBLE_ARROW
%type <t> T_PAAMAYIM_NEKUDOTAYIM
%type <t> T_METHOD_C
%type <t> T_WHILE
%type <t> T_END_HEREDOC
%type <t> T_FUNCTION
%type <t> '|'
%type <t> T_NEW
%type <t> T_REQUIRE
%type <t> T_LINE
%type <t> T_STATIC
%type <t> T_OBJECT_OPERATOR
%type <t> T_FUNC_C
%type <t> T_IMPLEMENTS
%type <t> T_IF
%type <t> T_MOD_EQUAL
%type <t> T_IS_SMALLER_OR_EQUAL
%type <t> T_AS
%type <t> ')'
%type <t> T_LIST
%type <t> T_USE

%expect 4

%left T_INCLUDE T_INCLUDE_ONCE T_EVAL T_REQUIRE T_REQUIRE_ONCE
%left ','
%left T_LOGICAL_OR
%left T_LOGICAL_XOR
%left T_LOGICAL_AND
%right T_PRINT
%left '=' T_PLUS_EQUAL T_MINUS_EQUAL T_MUL_EQUAL T_DIV_EQUAL T_CONCAT_EQUAL T_MOD_EQUAL T_AND_EQUAL T_OR_EQUAL T_XOR_EQUAL T_SL_EQUAL T_SR_EQUAL
%left '?' ':'
%left T_BOOLEAN_OR
%left T_BOOLEAN_AND
%left '|'
%left '^'
%left '&'
%nonassoc T_IS_EQUAL T_IS_NOT_EQUAL T_IS_IDENTICAL T_IS_NOT_IDENTICAL
%nonassoc '<' T_IS_SMALLER_OR_EQUAL '>' T_IS_GREATER_OR_EQUAL
%left T_SL T_SR
%left '+' '-' '.'
%left '*' '/' '%'
%right '!'
%nonassoc T_INSTANCEOF
%right '~' T_INC T_DEC T_INT_CAST T_DOUBLE_CAST T_STRING_CAST T_ARRAY_CAST T_OBJECT_CAST T_BOOL_CAST T_UNSET_CAST '@'
%right '['
%nonassoc T_NEW T_CLONE
%token T_EXIT
%token T_IF
%left T_ELSEIF
%left T_ELSE
%left T_ENDIF
%token T_LNUMBER
%token T_DNUMBER
%token T_STRING
%token T_STRING_VARNAME
%token T_VARIABLE
%token T_NUM_STRING
%token T_INLINE_HTML
%token T_CHARACTER
%token T_BAD_CHARACTER
%token T_ENCAPSED_AND_WHITESPACE
%token T_CONSTANT_ENCAPSED_STRING
%token T_ECHO
%token T_DO
%token T_WHILE
%token T_ENDWHILE
%token T_FOR
%token T_ENDFOR
%token T_FOREACH
%token T_ENDFOREACH
%token T_DECLARE
%token T_ENDDECLARE
%token T_AS
%token T_SWITCH
%token T_ENDSWITCH
%token T_CASE
%token T_DEFAULT
%token T_BREAK
%token T_CONTINUE
%token T_FUNCTION
%token T_CONST
%token T_RETURN
%token T_TRY
%token T_CATCH
%token T_THROW
%token T_USE
%token T_GLOBAL
%right T_STATIC T_ABSTRACT T_FINAL T_PRIVATE T_PROTECTED T_PUBLIC
%token T_VAR
%token T_UNSET
%token T_ISSET
%token T_EMPTY
%token T_HALT_COMPILER
%token T_CLASS
%token T_INTERFACE
%token T_EXTENDS
%token T_IMPLEMENTS
%token T_OBJECT_OPERATOR
%token T_DOUBLE_ARROW
%token T_LIST
%token T_ARRAY
%token T_CLASS_C
%token T_METHOD_C
%token T_FUNC_C
%token T_LINE
%token T_FILE
%token T_COMMENT
%token T_DOC_COMMENT
%token T_OPEN_TAG
%token T_OPEN_TAG_WITH_ECHO
%token T_CLOSE_TAG
%token T_WHITESPACE
%token T_START_HEREDOC
%token T_END_HEREDOC
%token T_DOLLAR_OPEN_CURLY_BRACES
%token T_CURLY_OPEN
%token T_PAAMAYIM_NEKUDOTAYIM


%%


start : top_statement_list 
    {
        $$= new NonTerminal( 55 );

        $$->addChild($1);

        $1->parent= $$;
root= $$;

    }
    ;

top_statement_list : top_statement_list top_statement 
    {
        $$= new NonTerminal( 65 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

top_statement_list : 
    {
        $$= new NonTerminal( 65 );

    }
    ;

top_statement : statement 
    {
        $$= new NonTerminal( 42 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

top_statement : function_declaration_statement 
    {
        $$= new NonTerminal( 42 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

top_statement : class_declaration_statement 
    {
        $$= new NonTerminal( 42 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

top_statement : T_HALT_COMPILER '(' ')' stmt_end 
    {
        $$= new NonTerminal( 42 );

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

inner_statement_list : inner_statement_list inner_statement 
    {
        $$= new NonTerminal( 27 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

inner_statement_list : 
    {
        $$= new NonTerminal( 27 );

    }
    ;

inner_statement : statement 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

inner_statement : function_declaration_statement 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

inner_statement : class_declaration_statement 
    {
        $$= new NonTerminal( 17 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

inner_statement : T_HALT_COMPILER '(' ')' stmt_end 
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

stmt_end : T_CLOSE_TAG 
    {
        $$= new NonTerminal( 59 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

stmt_end : ';' 
    {
        $$= new NonTerminal( 59 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

statement : unticked_statement 
    {
        $$= new NonTerminal( 57 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unticked_statement : '{' inner_statement_list '}' 
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

unticked_statement : T_IF '(' expr ')' statement elseif_list else_single 
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

unticked_statement : T_IF '(' expr ')' ':' inner_statement_list new_elseif_list new_else_single T_ENDIF stmt_end 
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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

        $8->nextSibbling= $9;

        $$->addChild($9);

        $9->parent= $$;

        $9->nextSibbling= $10;

        $$->addChild($10);

        $10->parent= $$;

    }
    ;

unticked_statement : T_WHILE '(' expr ')' while_statement 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

unticked_statement : T_DO statement T_WHILE '(' expr ')' stmt_end 
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

unticked_statement : T_FOR '(' for_expr ';' for_expr ';' for_expr ')' for_statement 
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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

        $8->nextSibbling= $9;

        $$->addChild($9);

        $9->parent= $$;

    }
    ;

unticked_statement : T_SWITCH '(' expr ')' switch_case_list 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

unticked_statement : T_BREAK stmt_end 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unticked_statement : T_BREAK expr stmt_end 
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

unticked_statement : T_CONTINUE stmt_end 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unticked_statement : T_CONTINUE expr stmt_end 
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

unticked_statement : T_RETURN stmt_end 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unticked_statement : T_RETURN expr_without_variable stmt_end 
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

unticked_statement : T_RETURN variable stmt_end 
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

unticked_statement : T_GLOBAL global_var_list stmt_end 
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

unticked_statement : T_STATIC static_var_list stmt_end 
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

unticked_statement : T_ECHO echo_expr_list stmt_end 
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

unticked_statement : T_INLINE_HTML 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unticked_statement : expr stmt_end 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

unticked_statement : T_USE use_filename stmt_end 
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

unticked_statement : T_UNSET '(' unset_variables ')' stmt_end 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

unticked_statement : T_FOREACH '(' variable T_AS foreach_variable foreach_optional_arg ')' foreach_statement 
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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

    }
    ;

unticked_statement : T_FOREACH '(' expr_without_variable T_AS variable foreach_optional_arg ')' foreach_statement 
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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

    }
    ;

unticked_statement : T_DECLARE '(' declare_list ')' declare_statement 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

unticked_statement : stmt_end 
    {
        $$= new NonTerminal( 28 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unticked_statement : T_TRY '{' inner_statement_list '}' T_CATCH '(' fully_qualified_class_name T_VARIABLE ')' '{' inner_statement_list '}' additional_catches 
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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

        $8->nextSibbling= $9;

        $$->addChild($9);

        $9->parent= $$;

        $9->nextSibbling= $10;

        $$->addChild($10);

        $10->parent= $$;

        $10->nextSibbling= $11;

        $$->addChild($11);

        $11->parent= $$;

        $11->nextSibbling= $12;

        $$->addChild($12);

        $12->parent= $$;

        $12->nextSibbling= $13;

        $$->addChild($13);

        $13->parent= $$;

    }
    ;

unticked_statement : T_THROW expr stmt_end 
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

additional_catches : non_empty_additional_catches 
    {
        $$= new NonTerminal( 92 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

additional_catches : 
    {
        $$= new NonTerminal( 92 );

    }
    ;

non_empty_additional_catches : additional_catch 
    {
        $$= new NonTerminal( 87 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_additional_catches : non_empty_additional_catches additional_catch 
    {
        $$= new NonTerminal( 87 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

additional_catch : T_CATCH '(' fully_qualified_class_name T_VARIABLE ')' '{' inner_statement_list '}' 
    {
        $$= new NonTerminal( 8 );

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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

    }
    ;

unset_variables : unset_variable 
    {
        $$= new NonTerminal( 4 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unset_variables : unset_variables ',' unset_variable 
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

unset_variable : variable 
    {
        $$= new NonTerminal( 62 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

use_filename : T_CONSTANT_ENCAPSED_STRING 
    {
        $$= new NonTerminal( 49 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

use_filename : '(' T_CONSTANT_ENCAPSED_STRING ')' 
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

    }
    ;

function_declaration_statement : unticked_function_declaration_statement 
    {
        $$= new NonTerminal( 56 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_declaration_statement : unticked_class_declaration_statement 
    {
        $$= new NonTerminal( 33 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

is_reference : 
    {
        $$= new NonTerminal( 64 );

    }
    ;

is_reference : '&' 
    {
        $$= new NonTerminal( 64 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

unticked_function_declaration_statement : T_FUNCTION is_reference T_STRING '(' parameter_list ')' '{' inner_statement_list '}' 
    {
        $$= new NonTerminal( 77 );

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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

        $8->nextSibbling= $9;

        $$->addChild($9);

        $9->parent= $$;

    }
    ;

unticked_class_declaration_statement : class_entry_type T_STRING extends_from implements_list '{' class_statement_list '}' 
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

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

        $6->nextSibbling= $7;

        $$->addChild($7);

        $7->parent= $$;

    }
    ;

unticked_class_declaration_statement : interface_entry T_STRING interface_extends_list '{' class_statement_list '}' 
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

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

class_entry_type : T_CLASS 
    {
        $$= new NonTerminal( 45 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_entry_type : T_ABSTRACT T_CLASS 
    {
        $$= new NonTerminal( 45 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_entry_type : T_FINAL T_CLASS 
    {
        $$= new NonTerminal( 45 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

extends_from : 
    {
        $$= new NonTerminal( 80 );

    }
    ;

extends_from : T_EXTENDS fully_qualified_class_name 
    {
        $$= new NonTerminal( 80 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_entry : T_INTERFACE 
    {
        $$= new NonTerminal( 66 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_extends_list : 
    {
        $$= new NonTerminal( 38 );

    }
    ;

interface_extends_list : T_EXTENDS interface_list 
    {
        $$= new NonTerminal( 38 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

implements_list : 
    {
        $$= new NonTerminal( 30 );

    }
    ;

implements_list : T_IMPLEMENTS interface_list 
    {
        $$= new NonTerminal( 30 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

interface_list : fully_qualified_class_name 
    {
        $$= new NonTerminal( 41 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

interface_list : interface_list ',' fully_qualified_class_name 
    {
        $$= new NonTerminal( 41 );

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

foreach_optional_arg : 
    {
        $$= new NonTerminal( 91 );

    }
    ;

foreach_optional_arg : T_DOUBLE_ARROW foreach_variable 
    {
        $$= new NonTerminal( 91 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

foreach_variable : variable 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

foreach_variable : '&' variable 
    {
        $$= new NonTerminal( 0 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

for_statement : statement 
    {
        $$= new NonTerminal( 7 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_statement : ':' inner_statement_list T_ENDFOR stmt_end 
    {
        $$= new NonTerminal( 7 );

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

foreach_statement : statement 
    {
        $$= new NonTerminal( 85 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

foreach_statement : ':' inner_statement_list T_ENDFOREACH stmt_end 
    {
        $$= new NonTerminal( 85 );

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

declare_statement : statement 
    {
        $$= new NonTerminal( 22 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

declare_statement : ':' inner_statement_list T_ENDDECLARE stmt_end 
    {
        $$= new NonTerminal( 22 );

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

declare_list : T_STRING '=' static_scalar 
    {
        $$= new NonTerminal( 95 );

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

declare_list : declare_list ',' T_STRING '=' static_scalar 
    {
        $$= new NonTerminal( 95 );

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

switch_case_list : '{' case_list '}' 
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

switch_case_list : '{' ';' case_list '}' 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

switch_case_list : ':' case_list T_ENDSWITCH stmt_end 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

switch_case_list : ':' ';' case_list T_ENDSWITCH stmt_end 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

case_list : 
    {
        $$= new NonTerminal( 48 );

    }
    ;

case_list : case_list T_CASE expr case_separator inner_statement_list 
    {
        $$= new NonTerminal( 48 );

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

case_list : case_list T_DEFAULT case_separator inner_statement_list 
    {
        $$= new NonTerminal( 48 );

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

case_separator : ':' 
    {
        $$= new NonTerminal( 93 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

case_separator : ';' 
    {
        $$= new NonTerminal( 93 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

while_statement : statement 
    {
        $$= new NonTerminal( 5 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

while_statement : ':' inner_statement_list T_ENDWHILE stmt_end 
    {
        $$= new NonTerminal( 5 );

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

elseif_list : 
    {
        $$= new NonTerminal( 46 );

    }
    ;

elseif_list : elseif_list T_ELSEIF '(' expr ')' statement 
    {
        $$= new NonTerminal( 46 );

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

new_elseif_list : 
    {
        $$= new NonTerminal( 79 );

    }
    ;

new_elseif_list : new_elseif_list T_ELSEIF '(' expr ')' ':' inner_statement_list 
    {
        $$= new NonTerminal( 79 );

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

else_single : 
    {
        $$= new NonTerminal( 25 );

    }
    ;

else_single : T_ELSE statement 
    {
        $$= new NonTerminal( 25 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

new_else_single : 
    {
        $$= new NonTerminal( 69 );

    }
    ;

new_else_single : T_ELSE ':' inner_statement_list 
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

parameter_list : non_empty_parameter_list 
    {
        $$= new NonTerminal( 12 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

parameter_list : 
    {
        $$= new NonTerminal( 12 );

    }
    ;

non_empty_parameter_list : optional_class_type T_VARIABLE 
    {
        $$= new NonTerminal( 34 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

non_empty_parameter_list : optional_class_type '&' T_VARIABLE 
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

    }
    ;

non_empty_parameter_list : optional_class_type '&' T_VARIABLE '=' static_scalar 
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

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

non_empty_parameter_list : optional_class_type T_VARIABLE '=' static_scalar 
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

non_empty_parameter_list : non_empty_parameter_list ',' optional_class_type T_VARIABLE 
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

non_empty_parameter_list : non_empty_parameter_list ',' optional_class_type '&' T_VARIABLE 
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

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

non_empty_parameter_list : non_empty_parameter_list ',' optional_class_type '&' T_VARIABLE '=' static_scalar 
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

non_empty_parameter_list : non_empty_parameter_list ',' optional_class_type T_VARIABLE '=' static_scalar 
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

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

        $5->nextSibbling= $6;

        $$->addChild($6);

        $6->parent= $$;

    }
    ;

optional_class_type : 
    {
        $$= new NonTerminal( 103 );

    }
    ;

optional_class_type : T_STRING 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

optional_class_type : T_ARRAY 
    {
        $$= new NonTerminal( 103 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

function_call_parameter_list : non_empty_function_call_parameter_list 
    {
        $$= new NonTerminal( 89 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

function_call_parameter_list : 
    {
        $$= new NonTerminal( 89 );

    }
    ;

non_empty_function_call_parameter_list : expr_without_variable 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_function_call_parameter_list : variable 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_function_call_parameter_list : '&' w_variable 
    {
        $$= new NonTerminal( 40 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

non_empty_function_call_parameter_list : non_empty_function_call_parameter_list ',' expr_without_variable 
    {
        $$= new NonTerminal( 40 );

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

non_empty_function_call_parameter_list : non_empty_function_call_parameter_list ',' variable 
    {
        $$= new NonTerminal( 40 );

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

non_empty_function_call_parameter_list : non_empty_function_call_parameter_list ',' '&' w_variable 
    {
        $$= new NonTerminal( 40 );

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

global_var_list : global_var_list ',' global_var 
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

    }
    ;

global_var_list : global_var 
    {
        $$= new NonTerminal( 74 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

global_var : T_VARIABLE 
    {
        $$= new NonTerminal( 63 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

global_var : '$' r_variable 
    {
        $$= new NonTerminal( 63 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

global_var : '$' '{' expr '}' 
    {
        $$= new NonTerminal( 63 );

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

static_var_list : static_var_list ',' T_VARIABLE 
    {
        $$= new NonTerminal( 31 );

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

static_var_list : static_var_list ',' T_VARIABLE '=' static_scalar 
    {
        $$= new NonTerminal( 31 );

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

static_var_list : T_VARIABLE 
    {
        $$= new NonTerminal( 31 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_var_list : T_VARIABLE '=' static_scalar 
    {
        $$= new NonTerminal( 31 );

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

class_statement_list : class_statement_list class_statement 
    {
        $$= new NonTerminal( 50 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_statement_list : 
    {
        $$= new NonTerminal( 50 );

    }
    ;

class_statement : variable_modifiers class_variable_declaration stmt_end 
    {
        $$= new NonTerminal( 47 );

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

class_statement : class_constant_declaration stmt_end 
    {
        $$= new NonTerminal( 47 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

class_statement : method_modifiers T_FUNCTION is_reference T_STRING '(' parameter_list ')' method_body 
    {
        $$= new NonTerminal( 47 );

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

        $7->nextSibbling= $8;

        $$->addChild($8);

        $8->parent= $$;

    }
    ;

method_body : stmt_end 
    {
        $$= new NonTerminal( 94 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_body : '{' inner_statement_list '}' 
    {
        $$= new NonTerminal( 94 );

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

variable_modifiers : non_empty_member_modifiers 
    {
        $$= new NonTerminal( 37 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_modifiers : T_VAR 
    {
        $$= new NonTerminal( 37 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

method_modifiers : 
    {
        $$= new NonTerminal( 73 );

    }
    ;

method_modifiers : non_empty_member_modifiers 
    {
        $$= new NonTerminal( 73 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_member_modifiers : member_modifier 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_member_modifiers : non_empty_member_modifiers member_modifier 
    {
        $$= new NonTerminal( 90 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

member_modifier : T_PUBLIC 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

member_modifier : T_PROTECTED 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

member_modifier : T_PRIVATE 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

member_modifier : T_STATIC 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

member_modifier : T_ABSTRACT 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

member_modifier : T_FINAL 
    {
        $$= new NonTerminal( 20 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_variable_declaration : class_variable_declaration ',' T_VARIABLE 
    {
        $$= new NonTerminal( 14 );

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

class_variable_declaration : class_variable_declaration ',' T_VARIABLE '=' static_scalar 
    {
        $$= new NonTerminal( 14 );

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

class_variable_declaration : T_VARIABLE 
    {
        $$= new NonTerminal( 14 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_variable_declaration : T_VARIABLE '=' static_scalar 
    {
        $$= new NonTerminal( 14 );

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

class_constant_declaration : class_constant_declaration ',' T_STRING '=' static_scalar 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

class_constant_declaration : T_CONST T_STRING '=' static_scalar 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

echo_expr_list : echo_expr_list ',' expr 
    {
        $$= new NonTerminal( 70 );

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

echo_expr_list : expr 
    {
        $$= new NonTerminal( 70 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

for_expr : 
    {
        $$= new NonTerminal( 102 );

    }
    ;

for_expr : non_empty_for_expr 
    {
        $$= new NonTerminal( 102 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_for_expr : non_empty_for_expr ',' expr 
    {
        $$= new NonTerminal( 44 );

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

non_empty_for_expr : expr 
    {
        $$= new NonTerminal( 44 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expr_without_variable : T_LIST '(' assignment_list ')' '=' expr 
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

expr_without_variable : variable '=' expr 
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

expr_without_variable : variable '=' '&' variable 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

expr_without_variable : variable '=' '&' T_NEW class_name_reference ctor_arguments 
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

expr_without_variable : T_NEW class_name_reference ctor_arguments 
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

expr_without_variable : T_CLONE expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : variable T_PLUS_EQUAL expr 
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

expr_without_variable : variable T_MINUS_EQUAL expr 
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

expr_without_variable : variable T_MUL_EQUAL expr 
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

expr_without_variable : variable T_DIV_EQUAL expr 
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

expr_without_variable : variable T_CONCAT_EQUAL expr 
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

expr_without_variable : variable T_MOD_EQUAL expr 
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

expr_without_variable : variable T_AND_EQUAL expr 
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

expr_without_variable : variable T_OR_EQUAL expr 
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

expr_without_variable : variable T_XOR_EQUAL expr 
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

expr_without_variable : variable T_SL_EQUAL expr 
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

expr_without_variable : variable T_SR_EQUAL expr 
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

expr_without_variable : rw_variable T_INC 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_INC rw_variable 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : rw_variable T_DEC 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_DEC rw_variable 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : expr T_BOOLEAN_OR expr 
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

expr_without_variable : expr T_BOOLEAN_AND expr 
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

expr_without_variable : expr T_LOGICAL_OR expr 
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

expr_without_variable : expr T_LOGICAL_AND expr 
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

expr_without_variable : expr T_LOGICAL_XOR expr 
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

expr_without_variable : expr '|' expr 
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

expr_without_variable : expr '&' expr 
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

expr_without_variable : expr '^' expr 
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

expr_without_variable : expr '.' expr 
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

expr_without_variable : expr '+' expr 
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

expr_without_variable : expr '-' expr 
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

expr_without_variable : expr '*' expr 
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

expr_without_variable : expr '/' expr 
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

expr_without_variable : expr '%' expr 
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

expr_without_variable : expr T_SL expr 
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

expr_without_variable : expr T_SR expr 
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

expr_without_variable : '+' expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : '-' expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : '!' expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : '~' expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : expr T_IS_IDENTICAL expr 
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

expr_without_variable : expr T_IS_NOT_IDENTICAL expr 
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

expr_without_variable : expr T_IS_EQUAL expr 
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

expr_without_variable : expr T_IS_NOT_EQUAL expr 
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

expr_without_variable : expr '<' expr 
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

expr_without_variable : expr T_IS_SMALLER_OR_EQUAL expr 
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

expr_without_variable : expr '>' expr 
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

expr_without_variable : expr T_IS_GREATER_OR_EQUAL expr 
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

expr_without_variable : expr T_INSTANCEOF class_name_reference 
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

expr_without_variable : '(' expr ')' 
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

expr_without_variable : expr '?' expr ':' expr 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

expr_without_variable : internal_functions_in_yacc 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expr_without_variable : T_INT_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_DOUBLE_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_STRING_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_ARRAY_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_OBJECT_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_BOOL_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_UNSET_CAST expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : T_EXIT exit_expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : '@' expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

expr_without_variable : scalar 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expr_without_variable : T_ARRAY '(' array_pair_list ')' 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

expr_without_variable : '`' encaps_list '`' 
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

expr_without_variable : T_PRINT expr 
    {
        $$= new NonTerminal( 51 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

function_call : T_STRING '(' function_call_parameter_list ')' 
    {
        $$= new NonTerminal( 19 );

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

function_call : fully_qualified_class_name T_PAAMAYIM_NEKUDOTAYIM T_STRING '(' function_call_parameter_list ')' 
    {
        $$= new NonTerminal( 19 );

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

function_call : fully_qualified_class_name T_PAAMAYIM_NEKUDOTAYIM variable_without_objects '(' function_call_parameter_list ')' 
    {
        $$= new NonTerminal( 19 );

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

function_call : variable_without_objects '(' function_call_parameter_list ')' 
    {
        $$= new NonTerminal( 19 );

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

fully_qualified_class_name : T_STRING 
    {
        $$= new NonTerminal( 86 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_name_reference : T_STRING 
    {
        $$= new NonTerminal( 24 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

class_name_reference : dynamic_class_name_reference 
    {
        $$= new NonTerminal( 24 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

dynamic_class_name_reference : base_variable T_OBJECT_OPERATOR object_property dynamic_class_name_variable_properties 
    {
        $$= new NonTerminal( 101 );

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

dynamic_class_name_reference : base_variable 
    {
        $$= new NonTerminal( 101 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

dynamic_class_name_variable_properties : dynamic_class_name_variable_properties dynamic_class_name_variable_property 
    {
        $$= new NonTerminal( 15 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

dynamic_class_name_variable_properties : 
    {
        $$= new NonTerminal( 15 );

    }
    ;

dynamic_class_name_variable_property : T_OBJECT_OPERATOR object_property 
    {
        $$= new NonTerminal( 16 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

exit_expr : 
    {
        $$= new NonTerminal( 9 );

    }
    ;

exit_expr : '(' ')' 
    {
        $$= new NonTerminal( 9 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

exit_expr : '(' expr ')' 
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

ctor_arguments : 
    {
        $$= new NonTerminal( 10 );

    }
    ;

ctor_arguments : '(' function_call_parameter_list ')' 
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

common_scalar : T_LNUMBER 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_DNUMBER 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_CONSTANT_ENCAPSED_STRING 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_LINE 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_FILE 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_CLASS_C 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_METHOD_C 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

common_scalar : T_FUNC_C 
    {
        $$= new NonTerminal( 82 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_scalar : common_scalar 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_scalar : T_STRING 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_scalar : '+' static_scalar 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

static_scalar : '-' static_scalar 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

static_scalar : T_ARRAY '(' static_array_pair_list ')' 
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

static_scalar : static_class_constant 
    {
        $$= new NonTerminal( 3 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

static_class_constant : T_STRING T_PAAMAYIM_NEKUDOTAYIM T_STRING 
    {
        $$= new NonTerminal( 32 );

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

scalar : T_STRING 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

scalar : T_STRING_VARNAME 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

scalar : class_constant 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

scalar : common_scalar 
    {
        $$= new NonTerminal( 1 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

scalar : '"' encaps_list '"' 
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

scalar : '\'' encaps_list '\'' 
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

scalar : T_START_HEREDOC encaps_list T_END_HEREDOC 
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

static_array_pair_list : 
    {
        $$= new NonTerminal( 100 );

    }
    ;

static_array_pair_list : non_empty_static_array_pair_list possible_comma 
    {
        $$= new NonTerminal( 100 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

possible_comma : 
    {
        $$= new NonTerminal( 81 );

    }
    ;

possible_comma : ',' 
    {
        $$= new NonTerminal( 81 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_static_array_pair_list : non_empty_static_array_pair_list ',' static_scalar T_DOUBLE_ARROW static_scalar 
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

non_empty_static_array_pair_list : non_empty_static_array_pair_list ',' static_scalar 
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

non_empty_static_array_pair_list : static_scalar T_DOUBLE_ARROW static_scalar 
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

non_empty_static_array_pair_list : static_scalar 
    {
        $$= new NonTerminal( 23 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expr : r_variable 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

expr : expr_without_variable 
    {
        $$= new NonTerminal( 71 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

r_variable : variable 
    {
        $$= new NonTerminal( 96 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

w_variable : variable 
    {
        $$= new NonTerminal( 2 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

rw_variable : variable 
    {
        $$= new NonTerminal( 52 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable : base_variable_with_function_calls T_OBJECT_OPERATOR object_property method_or_not variable_properties 
    {
        $$= new NonTerminal( 97 );

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

variable : base_variable_with_function_calls 
    {
        $$= new NonTerminal( 97 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_properties : variable_properties variable_property 
    {
        $$= new NonTerminal( 83 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

variable_properties : 
    {
        $$= new NonTerminal( 83 );

    }
    ;

variable_property : T_OBJECT_OPERATOR object_property method_or_not 
    {
        $$= new NonTerminal( 99 );

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

method_or_not : '(' function_call_parameter_list ')' 
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

method_or_not : 
    {
        $$= new NonTerminal( 98 );

    }
    ;

variable_without_objects : reference_variable 
    {
        $$= new NonTerminal( 84 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_without_objects : simple_indirect_reference reference_variable 
    {
        $$= new NonTerminal( 84 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

static_member : fully_qualified_class_name T_PAAMAYIM_NEKUDOTAYIM variable_without_objects 
    {
        $$= new NonTerminal( 88 );

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

base_variable_with_function_calls : base_variable 
    {
        $$= new NonTerminal( 26 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

base_variable_with_function_calls : function_call 
    {
        $$= new NonTerminal( 26 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

base_variable : reference_variable 
    {
        $$= new NonTerminal( 53 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

base_variable : simple_indirect_reference reference_variable 
    {
        $$= new NonTerminal( 53 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

base_variable : static_member 
    {
        $$= new NonTerminal( 53 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

reference_variable : reference_variable '[' dim_offset ']' 
    {
        $$= new NonTerminal( 6 );

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

reference_variable : reference_variable '{' expr '}' 
    {
        $$= new NonTerminal( 6 );

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

reference_variable : compound_variable 
    {
        $$= new NonTerminal( 6 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compound_variable : T_VARIABLE 
    {
        $$= new NonTerminal( 72 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

compound_variable : '$' '{' expr '}' 
    {
        $$= new NonTerminal( 72 );

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

dim_offset : 
    {
        $$= new NonTerminal( 61 );

    }
    ;

dim_offset : expr 
    {
        $$= new NonTerminal( 61 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

object_property : object_dim_list 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

object_property : variable_without_objects 
    {
        $$= new NonTerminal( 29 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

object_dim_list : object_dim_list '[' dim_offset ']' 
    {
        $$= new NonTerminal( 36 );

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

object_dim_list : object_dim_list '{' expr '}' 
    {
        $$= new NonTerminal( 36 );

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

object_dim_list : variable_name 
    {
        $$= new NonTerminal( 36 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_name : T_STRING 
    {
        $$= new NonTerminal( 58 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

variable_name : '{' expr '}' 
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

simple_indirect_reference : '$' 
    {
        $$= new NonTerminal( 67 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

simple_indirect_reference : simple_indirect_reference '$' 
    {
        $$= new NonTerminal( 67 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

assignment_list : assignment_list ',' assignment_list_element 
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

assignment_list : assignment_list_element 
    {
        $$= new NonTerminal( 39 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment_list_element : variable 
    {
        $$= new NonTerminal( 13 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

assignment_list_element : T_LIST '(' assignment_list ')' 
    {
        $$= new NonTerminal( 13 );

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

assignment_list_element : 
    {
        $$= new NonTerminal( 13 );

    }
    ;

array_pair_list : 
    {
        $$= new NonTerminal( 68 );

    }
    ;

array_pair_list : non_empty_array_pair_list possible_comma 
    {
        $$= new NonTerminal( 68 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

non_empty_array_pair_list : non_empty_array_pair_list ',' expr T_DOUBLE_ARROW expr 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

        $4->nextSibbling= $5;

        $$->addChild($5);

        $5->parent= $$;

    }
    ;

non_empty_array_pair_list : non_empty_array_pair_list ',' expr 
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

non_empty_array_pair_list : expr T_DOUBLE_ARROW expr 
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

non_empty_array_pair_list : expr 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

non_empty_array_pair_list : non_empty_array_pair_list ',' expr T_DOUBLE_ARROW '&' w_variable 
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

non_empty_array_pair_list : non_empty_array_pair_list ',' '&' w_variable 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

non_empty_array_pair_list : expr T_DOUBLE_ARROW '&' w_variable 
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

        $3->nextSibbling= $4;

        $$->addChild($4);

        $4->parent= $$;

    }
    ;

non_empty_array_pair_list : '&' w_variable 
    {
        $$= new NonTerminal( 60 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list encaps_var 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_STRING 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_NUM_STRING 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_ENCAPSED_AND_WHITESPACE 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_CHARACTER 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_BAD_CHARACTER 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list '[' 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list ']' 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list '{' 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list '}' 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : encaps_list T_OBJECT_OPERATOR 
    {
        $$= new NonTerminal( 78 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

encaps_list : 
    {
        $$= new NonTerminal( 78 );

    }
    ;

encaps_var : T_VARIABLE 
    {
        $$= new NonTerminal( 11 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

encaps_var : T_VARIABLE '[' encaps_var_offset ']' 
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

    }
    ;

encaps_var : T_VARIABLE T_OBJECT_OPERATOR T_STRING 
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

    }
    ;

encaps_var : T_DOLLAR_OPEN_CURLY_BRACES expr '}' 
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

    }
    ;

encaps_var : T_DOLLAR_OPEN_CURLY_BRACES T_STRING_VARNAME '[' expr ']' '}' 
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

encaps_var : T_CURLY_OPEN variable '}' 
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

    }
    ;

encaps_var_offset : T_STRING 
    {
        $$= new NonTerminal( 43 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

encaps_var_offset : T_NUM_STRING 
    {
        $$= new NonTerminal( 43 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

encaps_var_offset : T_VARIABLE 
    {
        $$= new NonTerminal( 43 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

internal_functions_in_yacc : T_ISSET '(' isset_variables ')' 
    {
        $$= new NonTerminal( 21 );

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

internal_functions_in_yacc : T_EMPTY '(' variable ')' 
    {
        $$= new NonTerminal( 21 );

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

internal_functions_in_yacc : T_INCLUDE expr 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

internal_functions_in_yacc : T_INCLUDE_ONCE expr 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

internal_functions_in_yacc : T_EVAL '(' expr ')' 
    {
        $$= new NonTerminal( 21 );

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

internal_functions_in_yacc : T_REQUIRE expr 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

internal_functions_in_yacc : T_REQUIRE_ONCE expr 
    {
        $$= new NonTerminal( 21 );

        $$->addChild($1);

        $1->parent= $$;

        $1->nextSibbling= $2;

        $$->addChild($2);

        $2->parent= $$;

    }
    ;

isset_variables : variable 
    {
        $$= new NonTerminal( 54 );

        $$->addChild($1);

        $1->parent= $$;

    }
    ;

isset_variables : isset_variables ',' variable 
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

class_constant : fully_qualified_class_name T_PAAMAYIM_NEKUDOTAYIM T_STRING 
    {
        $$= new NonTerminal( 18 );

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


