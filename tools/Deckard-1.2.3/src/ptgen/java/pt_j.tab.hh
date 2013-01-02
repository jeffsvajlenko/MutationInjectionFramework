/* A Bison parser, made by GNU Bison 2.5.  */

/* Bison interface for Yacc-like parsers in C
   
      Copyright (C) 1984, 1989-1990, 2000-2011 Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     PLUS_TK = 258,
     MINUS_TK = 259,
     MULT_TK = 260,
     DIV_TK = 261,
     REM_TK = 262,
     LS_TK = 263,
     SRS_TK = 264,
     ZRS_TK = 265,
     AND_TK = 266,
     XOR_TK = 267,
     OR_TK = 268,
     BOOL_AND_TK = 269,
     BOOL_OR_TK = 270,
     EQ_TK = 271,
     NEQ_TK = 272,
     GT_TK = 273,
     GTE_TK = 274,
     LT_TK = 275,
     LTE_TK = 276,
     PLUS_ASSIGN_TK = 277,
     MINUS_ASSIGN_TK = 278,
     MULT_ASSIGN_TK = 279,
     DIV_ASSIGN_TK = 280,
     REM_ASSIGN_TK = 281,
     LS_ASSIGN_TK = 282,
     SRS_ASSIGN_TK = 283,
     ZRS_ASSIGN_TK = 284,
     AND_ASSIGN_TK = 285,
     XOR_ASSIGN_TK = 286,
     OR_ASSIGN_TK = 287,
     PUBLIC_TK = 288,
     PRIVATE_TK = 289,
     PROTECTED_TK = 290,
     STATIC_TK = 291,
     FINAL_TK = 292,
     SYNCHRONIZED_TK = 293,
     VOLATILE_TK = 294,
     TRANSIENT_TK = 295,
     NATIVE_TK = 296,
     PAD_TK = 297,
     ABSTRACT_TK = 298,
     STRICT_TK = 299,
     MODIFIER_TK = 300,
     DECR_TK = 301,
     INCR_TK = 302,
     DEFAULT_TK = 303,
     IF_TK = 304,
     THROW_TK = 305,
     BOOLEAN_TK = 306,
     DO_TK = 307,
     IMPLEMENTS_TK = 308,
     THROWS_TK = 309,
     BREAK_TK = 310,
     IMPORT_TK = 311,
     ELSE_TK = 312,
     INSTANCEOF_TK = 313,
     RETURN_TK = 314,
     VOID_TK = 315,
     CATCH_TK = 316,
     INTERFACE_TK = 317,
     CASE_TK = 318,
     EXTENDS_TK = 319,
     FINALLY_TK = 320,
     SUPER_TK = 321,
     WHILE_TK = 322,
     CLASS_TK = 323,
     SWITCH_TK = 324,
     CONST_TK = 325,
     TRY_TK = 326,
     FOR_TK = 327,
     NEW_TK = 328,
     CONTINUE_TK = 329,
     GOTO_TK = 330,
     PACKAGE_TK = 331,
     THIS_TK = 332,
     ASSERT_TK = 333,
     BYTE_TK = 334,
     SHORT_TK = 335,
     INT_TK = 336,
     LONG_TK = 337,
     CHAR_TK = 338,
     INTEGRAL_TK = 339,
     FLOAT_TK = 340,
     DOUBLE_TK = 341,
     FP_TK = 342,
     ID_TK = 343,
     REL_QM_TK = 344,
     REL_CL_TK = 345,
     NOT_TK = 346,
     NEG_TK = 347,
     ASSIGN_ANY_TK = 348,
     ASSIGN_TK = 349,
     OP_TK = 350,
     CP_TK = 351,
     OCB_TK = 352,
     CCB_TK = 353,
     OSB_TK = 354,
     CSB_TK = 355,
     SC_TK = 356,
     C_TK = 357,
     DOT_TK = 358,
     STRING_LIT_TK = 359,
     CHAR_LIT_TK = 360,
     INT_LIT_TK = 361,
     FP_LIT_TK = 362,
     TRUE_TK = 363,
     FALSE_TK = 364,
     BOOL_LIT_TK = 365,
     NULL_TK = 366
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 2068 of yacc.c  */
#line 10 "pt_j.y"

Tree *t;



/* Line 2068 of yacc.c  */
#line 167 "pt_j.tab.hh"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif




