/* A Bison parser, made by GNU Bison 2.5.  */

/* Bison implementation for Yacc-like parsers in C
   
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

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.5"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 1

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1

/* Using locations.  */
#define YYLSP_NEEDED 0



/* Copy the first part of user declarations.  */

/* Line 268 of yacc.c  */
#line 4 "pt_zend_language_parser.y"

#include<ptree.h>

using namespace std;


/* Line 268 of yacc.c  */
#line 78 "pt_zend_language_parser.tab.cc"

/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     T_REQUIRE_ONCE = 258,
     T_REQUIRE = 259,
     T_EVAL = 260,
     T_INCLUDE_ONCE = 261,
     T_INCLUDE = 262,
     T_LOGICAL_OR = 263,
     T_LOGICAL_XOR = 264,
     T_LOGICAL_AND = 265,
     T_PRINT = 266,
     T_SR_EQUAL = 267,
     T_SL_EQUAL = 268,
     T_XOR_EQUAL = 269,
     T_OR_EQUAL = 270,
     T_AND_EQUAL = 271,
     T_MOD_EQUAL = 272,
     T_CONCAT_EQUAL = 273,
     T_DIV_EQUAL = 274,
     T_MUL_EQUAL = 275,
     T_MINUS_EQUAL = 276,
     T_PLUS_EQUAL = 277,
     T_BOOLEAN_OR = 278,
     T_BOOLEAN_AND = 279,
     T_IS_NOT_IDENTICAL = 280,
     T_IS_IDENTICAL = 281,
     T_IS_NOT_EQUAL = 282,
     T_IS_EQUAL = 283,
     T_IS_GREATER_OR_EQUAL = 284,
     T_IS_SMALLER_OR_EQUAL = 285,
     T_SR = 286,
     T_SL = 287,
     T_INSTANCEOF = 288,
     T_UNSET_CAST = 289,
     T_BOOL_CAST = 290,
     T_OBJECT_CAST = 291,
     T_ARRAY_CAST = 292,
     T_STRING_CAST = 293,
     T_DOUBLE_CAST = 294,
     T_INT_CAST = 295,
     T_DEC = 296,
     T_INC = 297,
     T_CLONE = 298,
     T_NEW = 299,
     T_EXIT = 300,
     T_IF = 301,
     T_ELSEIF = 302,
     T_ELSE = 303,
     T_ENDIF = 304,
     T_LNUMBER = 305,
     T_DNUMBER = 306,
     T_STRING = 307,
     T_STRING_VARNAME = 308,
     T_VARIABLE = 309,
     T_NUM_STRING = 310,
     T_INLINE_HTML = 311,
     T_CHARACTER = 312,
     T_BAD_CHARACTER = 313,
     T_ENCAPSED_AND_WHITESPACE = 314,
     T_CONSTANT_ENCAPSED_STRING = 315,
     T_ECHO = 316,
     T_DO = 317,
     T_WHILE = 318,
     T_ENDWHILE = 319,
     T_FOR = 320,
     T_ENDFOR = 321,
     T_FOREACH = 322,
     T_ENDFOREACH = 323,
     T_DECLARE = 324,
     T_ENDDECLARE = 325,
     T_AS = 326,
     T_SWITCH = 327,
     T_ENDSWITCH = 328,
     T_CASE = 329,
     T_DEFAULT = 330,
     T_BREAK = 331,
     T_CONTINUE = 332,
     T_FUNCTION = 333,
     T_CONST = 334,
     T_RETURN = 335,
     T_TRY = 336,
     T_CATCH = 337,
     T_THROW = 338,
     T_USE = 339,
     T_GLOBAL = 340,
     T_PUBLIC = 341,
     T_PROTECTED = 342,
     T_PRIVATE = 343,
     T_FINAL = 344,
     T_ABSTRACT = 345,
     T_STATIC = 346,
     T_VAR = 347,
     T_UNSET = 348,
     T_ISSET = 349,
     T_EMPTY = 350,
     T_HALT_COMPILER = 351,
     T_CLASS = 352,
     T_INTERFACE = 353,
     T_EXTENDS = 354,
     T_IMPLEMENTS = 355,
     T_OBJECT_OPERATOR = 356,
     T_DOUBLE_ARROW = 357,
     T_LIST = 358,
     T_ARRAY = 359,
     T_CLASS_C = 360,
     T_METHOD_C = 361,
     T_FUNC_C = 362,
     T_LINE = 363,
     T_FILE = 364,
     T_COMMENT = 365,
     T_DOC_COMMENT = 366,
     T_OPEN_TAG = 367,
     T_OPEN_TAG_WITH_ECHO = 368,
     T_CLOSE_TAG = 369,
     T_WHITESPACE = 370,
     T_START_HEREDOC = 371,
     T_END_HEREDOC = 372,
     T_DOLLAR_OPEN_CURLY_BRACES = 373,
     T_CURLY_OPEN = 374,
     T_PAAMAYIM_NEKUDOTAYIM = 375
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 293 of yacc.c  */
#line 10 "pt_zend_language_parser.y"

Tree *t;



/* Line 293 of yacc.c  */
#line 240 "pt_zend_language_parser.tab.cc"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif


/* Copy the second part of user declarations.  */

/* Line 343 of yacc.c  */
#line 14 "pt_zend_language_parser.y"

void yyerror(char*s);
int yylex(YYSTYPE *yylvalp);

Tree *root;


/* Line 343 of yacc.c  */
#line 260 "pt_zend_language_parser.tab.cc"

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int yyi)
#else
static int
YYID (yyi)
    int yyi;
#endif
{
  return yyi;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)				\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack_alloc, Stack, yysize);			\
	Stack = &yyptr->Stack_alloc;					\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  3
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   6230

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  150
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  105
/* YYNRULES -- Number of rules.  */
#define YYNRULES  360
/* YYNRULES -- Number of states.  */
#define YYNSTATES  723

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   375

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,    28,    12,     2,    17,    10,    24,     8,
       5,    31,    14,     9,    20,    18,     6,    25,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     7,     4,
      19,    15,    23,    21,    22,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,    26,     2,    29,    13,     2,    11,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,    16,    30,    27,     3,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,    32,    33,
      34,    35,    36,    37,    38,    39,    40,    41,    42,    43,
      44,    45,    46,    47,    48,    49,    50,    51,    52,    53,
      54,    55,    56,    57,    58,    59,    60,    61,    62,    63,
      64,    65,    66,    67,    68,    69,    70,    71,    72,    73,
      74,    75,    76,    77,    78,    79,    80,    81,    82,    83,
      84,    85,    86,    87,    88,    89,    90,    91,    92,    93,
      94,    95,    96,    97,    98,    99,   100,   101,   102,   103,
     104,   105,   106,   107,   108,   109,   110,   111,   112,   113,
     114,   115,   116,   117,   118,   119,   120,   121,   122,   123,
     124,   125,   126,   127,   128,   129,   130,   131,   132,   133,
     134,   135,   136,   137,   138,   139,   140,   141,   142,   143,
     144,   145,   146,   147,   148,   149
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint16 yyprhs[] =
{
       0,     0,     3,     5,     8,     9,    11,    13,    15,    20,
      23,    24,    26,    28,    30,    35,    37,    39,    41,    45,
      53,    64,    70,    78,    88,    94,    97,   101,   104,   108,
     111,   115,   119,   123,   127,   131,   133,   136,   140,   146,
     155,   164,   170,   172,   186,   190,   192,   193,   195,   198,
     207,   209,   213,   215,   217,   221,   223,   225,   226,   228,
     238,   246,   253,   255,   258,   261,   262,   265,   267,   268,
     271,   272,   275,   277,   281,   282,   285,   287,   290,   292,
     297,   299,   304,   306,   311,   315,   321,   325,   330,   335,
     341,   342,   348,   353,   355,   357,   359,   364,   365,   372,
     373,   381,   382,   385,   386,   390,   392,   393,   396,   400,
     406,   411,   416,   422,   430,   437,   438,   440,   442,   444,
     445,   447,   449,   452,   456,   460,   465,   469,   471,   473,
     476,   481,   485,   491,   493,   497,   500,   501,   505,   508,
     517,   519,   523,   525,   527,   528,   530,   532,   535,   537,
     539,   541,   543,   545,   547,   551,   557,   559,   563,   569,
     574,   578,   580,   581,   583,   587,   589,   596,   600,   605,
     612,   616,   619,   623,   627,   631,   635,   639,   643,   647,
     651,   655,   659,   663,   666,   669,   672,   675,   679,   683,
     687,   691,   695,   699,   703,   707,   711,   715,   719,   723,
     727,   731,   735,   739,   742,   745,   748,   751,   755,   759,
     763,   767,   771,   775,   779,   783,   787,   791,   797,   799,
     802,   805,   808,   811,   814,   817,   820,   823,   826,   828,
     833,   837,   840,   845,   852,   859,   864,   866,   868,   870,
     875,   877,   880,   881,   884,   885,   888,   892,   893,   897,
     899,   901,   903,   905,   907,   909,   911,   913,   915,   917,
     920,   923,   928,   930,   934,   936,   938,   940,   942,   946,
     950,   954,   955,   958,   959,   961,   967,   971,   975,   977,
     979,   981,   983,   985,   987,   993,   995,   998,   999,  1003,
    1007,  1008,  1010,  1013,  1017,  1019,  1021,  1023,  1026,  1028,
    1033,  1038,  1040,  1042,  1047,  1048,  1050,  1052,  1054,  1059,
    1064,  1066,  1068,  1072,  1074,  1077,  1081,  1083,  1085,  1090,
    1091,  1092,  1095,  1101,  1105,  1109,  1111,  1118,  1123,  1128,
    1131,  1134,  1137,  1140,  1143,  1146,  1149,  1152,  1155,  1158,
    1161,  1164,  1165,  1167,  1172,  1176,  1180,  1187,  1191,  1193,
    1195,  1197,  1202,  1207,  1210,  1213,  1218,  1221,  1224,  1226,
    1230
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int16 yyrhs[] =
{
     151,     0,    -1,   152,    -1,   152,   153,    -1,    -1,   157,
      -1,   165,    -1,   166,    -1,   125,     5,    31,   156,    -1,
     154,   155,    -1,    -1,   157,    -1,   165,    -1,   166,    -1,
     125,     5,    31,   156,    -1,   143,    -1,     4,    -1,   158,
      -1,    16,   154,    27,    -1,    75,     5,   226,    31,   157,
     186,   188,    -1,    75,     5,   226,    31,     7,   154,   187,
     189,    78,   156,    -1,    92,     5,   226,    31,   185,    -1,
      91,   157,    92,     5,   226,    31,   156,    -1,    94,     5,
     208,     4,   208,     4,   208,    31,   178,    -1,   101,     5,
     226,    31,   182,    -1,   105,   156,    -1,   105,   226,   156,
      -1,   106,   156,    -1,   106,   226,   156,    -1,   109,   156,
      -1,   109,   210,   156,    -1,   109,   230,   156,    -1,   114,
     195,   156,    -1,   120,   197,   156,    -1,    90,   207,   156,
      -1,    85,    -1,   226,   156,    -1,   113,   164,   156,    -1,
     122,     5,   162,    31,   156,    -1,    96,     5,   230,   100,
     177,   176,    31,   179,    -1,    96,     5,   210,   100,   230,
     176,    31,   179,    -1,    98,     5,   181,    31,   180,    -1,
     156,    -1,   110,    16,   154,    27,   111,     5,   212,    83,
      31,    16,   154,    27,   159,    -1,   112,   226,   156,    -1,
     160,    -1,    -1,   161,    -1,   160,   161,    -1,   111,     5,
     212,    83,    31,    16,   154,    27,    -1,   163,    -1,   162,
      20,   163,    -1,   230,    -1,    89,    -1,     5,    89,    31,
      -1,   168,    -1,   169,    -1,    -1,    24,    -1,   107,   167,
      81,     5,   190,    31,    16,   154,    27,    -1,   170,    81,
     171,   174,    16,   198,    27,    -1,   172,    81,   173,    16,
     198,    27,    -1,   126,    -1,   119,   126,    -1,   118,   126,
      -1,    -1,   128,   212,    -1,   127,    -1,    -1,   128,   175,
      -1,    -1,   129,   175,    -1,   212,    -1,   175,    20,   212,
      -1,    -1,   131,   177,    -1,   230,    -1,    24,   230,    -1,
     157,    -1,     7,   154,    95,   156,    -1,   157,    -1,     7,
     154,    97,   156,    -1,   157,    -1,     7,   154,    99,   156,
      -1,    81,    15,   220,    -1,   181,    20,    81,    15,   220,
      -1,    16,   183,    27,    -1,    16,     4,   183,    27,    -1,
       7,   183,   102,   156,    -1,     7,     4,   183,   102,   156,
      -1,    -1,   183,   103,   226,   184,   154,    -1,   183,   104,
     184,   154,    -1,     7,    -1,     4,    -1,   157,    -1,     7,
     154,    93,   156,    -1,    -1,   186,    76,     5,   226,    31,
     157,    -1,    -1,   187,    76,     5,   226,    31,     7,   154,
      -1,    -1,    77,   157,    -1,    -1,    77,     7,   154,    -1,
     191,    -1,    -1,   192,    83,    -1,   192,    24,    83,    -1,
     192,    24,    83,    15,   220,    -1,   192,    83,    15,   220,
      -1,   191,    20,   192,    83,    -1,   191,    20,   192,    24,
      83,    -1,   191,    20,   192,    24,    83,    15,   220,    -1,
     191,    20,   192,    83,    15,   220,    -1,    -1,    81,    -1,
     133,    -1,   194,    -1,    -1,   210,    -1,   230,    -1,    24,
     228,    -1,   194,    20,   210,    -1,   194,    20,   230,    -1,
     194,    20,    24,   228,    -1,   195,    20,   196,    -1,   196,
      -1,    83,    -1,    17,   227,    -1,    17,    16,   226,    27,
      -1,   197,    20,    83,    -1,   197,    20,    83,    15,   220,
      -1,    83,    -1,    83,    15,   220,    -1,   198,   199,    -1,
      -1,   201,   205,   156,    -1,   206,   156,    -1,   202,   107,
     167,    81,     5,   190,    31,   200,    -1,   156,    -1,    16,
     154,    27,    -1,   203,    -1,   121,    -1,    -1,   203,    -1,
     204,    -1,   203,   204,    -1,   115,    -1,   116,    -1,   117,
      -1,   120,    -1,   119,    -1,   118,    -1,   205,    20,    83,
      -1,   205,    20,    83,    15,   220,    -1,    83,    -1,    83,
      15,   220,    -1,   206,    20,    81,    15,   220,    -1,   108,
      81,    15,   220,    -1,   207,    20,   226,    -1,   226,    -1,
      -1,   209,    -1,   209,    20,   226,    -1,   226,    -1,   132,
       5,   245,    31,    15,   226,    -1,   230,    15,   226,    -1,
     230,    15,    24,   230,    -1,   230,    15,    24,    73,   213,
     218,    -1,    73,   213,   218,    -1,    72,   226,    -1,   230,
      51,   226,    -1,   230,    50,   226,    -1,   230,    49,   226,
      -1,   230,    48,   226,    -1,   230,    47,   226,    -1,   230,
      46,   226,    -1,   230,    45,   226,    -1,   230,    44,   226,
      -1,   230,    43,   226,    -1,   230,    42,   226,    -1,   230,
      41,   226,    -1,   229,    71,    -1,    71,   229,    -1,   229,
      70,    -1,    70,   229,    -1,   226,    52,   226,    -1,   226,
      53,   226,    -1,   226,    37,   226,    -1,   226,    39,   226,
      -1,   226,    38,   226,    -1,   226,    30,   226,    -1,   226,
      24,   226,    -1,   226,    13,   226,    -1,   226,     6,   226,
      -1,   226,     9,   226,    -1,   226,    18,   226,    -1,   226,
      14,   226,    -1,   226,    25,   226,    -1,   226,    10,   226,
      -1,   226,    61,   226,    -1,   226,    60,   226,    -1,     9,
     226,    -1,    18,   226,    -1,    28,   226,    -1,     3,   226,
      -1,   226,    55,   226,    -1,   226,    54,   226,    -1,   226,
      57,   226,    -1,   226,    56,   226,    -1,   226,    19,   226,
      -1,   226,    59,   226,    -1,   226,    23,   226,    -1,   226,
      58,   226,    -1,   226,    62,   213,    -1,     5,   226,    31,
      -1,   226,    21,   226,     7,   226,    -1,   252,    -1,    69,
     226,    -1,    68,   226,    -1,    67,   226,    -1,    66,   226,
      -1,    65,   226,    -1,    64,   226,    -1,    63,   226,    -1,
      74,   217,    -1,    22,   226,    -1,   222,    -1,   133,     5,
     247,    31,    -1,    11,   249,    11,    -1,    40,   226,    -1,
      81,     5,   193,    31,    -1,   212,   149,    81,     5,   193,
      31,    -1,   212,   149,   234,     5,   193,    31,    -1,   234,
       5,   193,    31,    -1,    81,    -1,    81,    -1,   214,    -1,
     237,   130,   241,   215,    -1,   237,    -1,   215,   216,    -1,
      -1,   130,   241,    -1,    -1,     5,    31,    -1,     5,   226,
      31,    -1,    -1,     5,   193,    31,    -1,    79,    -1,    80,
      -1,    89,    -1,   137,    -1,   138,    -1,   134,    -1,   135,
      -1,   136,    -1,   219,    -1,    81,    -1,     9,   220,    -1,
      18,   220,    -1,   133,     5,   223,    31,    -1,   221,    -1,
      81,   149,    81,    -1,    81,    -1,    82,    -1,   254,    -1,
     219,    -1,    12,   249,    12,    -1,     8,   249,     8,    -1,
     145,   249,   146,    -1,    -1,   225,   224,    -1,    -1,    20,
      -1,   225,    20,   220,   131,   220,    -1,   225,    20,   220,
      -1,   220,   131,   220,    -1,   220,    -1,   227,    -1,   210,
      -1,   230,    -1,   230,    -1,   230,    -1,   236,   130,   241,
     233,   231,    -1,   236,    -1,   231,   232,    -1,    -1,   130,
     241,   233,    -1,     5,   193,    31,    -1,    -1,   238,    -1,
     244,   238,    -1,   212,   149,   234,    -1,   237,    -1,   211,
      -1,   238,    -1,   244,   238,    -1,   235,    -1,   238,    26,
     240,    29,    -1,   238,    16,   226,    27,    -1,   239,    -1,
      83,    -1,    17,    16,   226,    27,    -1,    -1,   226,    -1,
     242,    -1,   234,    -1,   242,    26,   240,    29,    -1,   242,
      16,   226,    27,    -1,   243,    -1,    81,    -1,    16,   226,
      27,    -1,    17,    -1,   244,    17,    -1,   245,    20,   246,
      -1,   246,    -1,   230,    -1,   132,     5,   245,    31,    -1,
      -1,    -1,   248,   224,    -1,   248,    20,   226,   131,   226,
      -1,   248,    20,   226,    -1,   226,   131,   226,    -1,   226,
      -1,   248,    20,   226,   131,    24,   228,    -1,   248,    20,
      24,   228,    -1,   226,   131,    24,   228,    -1,    24,   228,
      -1,   249,   250,    -1,   249,    81,    -1,   249,    84,    -1,
     249,    88,    -1,   249,    86,    -1,   249,    87,    -1,   249,
      26,    -1,   249,    29,    -1,   249,    16,    -1,   249,    27,
      -1,   249,   130,    -1,    -1,    83,    -1,    83,    26,   251,
      29,    -1,    83,   130,    81,    -1,   147,   226,    27,    -1,
     147,    82,    26,   226,    29,    27,    -1,   148,   230,    27,
      -1,    81,    -1,    84,    -1,    83,    -1,   123,     5,   253,
      31,    -1,   124,     5,   230,    31,    -1,    36,   226,    -1,
      35,   226,    -1,    34,     5,   226,    31,    -1,    33,   226,
      -1,    32,   226,    -1,   230,    -1,   253,    20,   230,    -1,
     212,   149,    81,    -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   370,   370,   382,   400,   406,   417,   428,   439,   468,
     486,   492,   503,   514,   525,   554,   565,   576,   587,   610,
     657,   722,   757,   804,   863,   898,   915,   938,   955,   978,
     995,  1018,  1041,  1064,  1087,  1110,  1121,  1138,  1161,  1196,
    1249,  1302,  1337,  1348,  1431,  1454,  1466,  1472,  1483,  1500,
    1553,  1564,  1587,  1598,  1609,  1632,  1643,  1655,  1661,  1672,
    1731,  1778,  1819,  1830,  1847,  1865,  1871,  1888,  1900,  1906,
    1924,  1930,  1947,  1958,  1982,  1988,  2005,  2016,  2033,  2044,
    2073,  2084,  2113,  2124,  2153,  2176,  2211,  2234,  2263,  2292,
    2328,  2334,  2369,  2398,  2409,  2420,  2431,  2461,  2467,  2509,
    2515,  2563,  2569,  2587,  2593,  2616,  2628,  2634,  2651,  2674,
    2709,  2738,  2767,  2802,  2849,  2891,  2897,  2908,  2919,  2931,
    2937,  2948,  2959,  2976,  2999,  3022,  3051,  3074,  3085,  3096,
    3113,  3142,  3165,  3200,  3211,  3234,  3252,  3258,  3281,  3298,
    3351,  3362,  3385,  3396,  3408,  3414,  3425,  3436,  3453,  3464,
    3475,  3486,  3497,  3508,  3519,  3542,  3577,  3588,  3611,  3646,
    3675,  3698,  3710,  3716,  3727,  3750,  3761,  3802,  3825,  3854,
    3895,  3918,  3935,  3958,  3981,  4004,  4027,  4050,  4073,  4096,
    4119,  4142,  4165,  4188,  4205,  4222,  4239,  4256,  4279,  4302,
    4325,  4348,  4371,  4394,  4417,  4440,  4463,  4486,  4509,  4532,
    4555,  4578,  4601,  4624,  4641,  4658,  4675,  4692,  4715,  4738,
    4761,  4784,  4807,  4830,  4853,  4876,  4899,  4922,  4957,  4968,
    4985,  5002,  5019,  5036,  5053,  5070,  5087,  5104,  5121,  5132,
    5161,  5184,  5201,  5230,  5271,  5312,  5341,  5352,  5363,  5374,
    5403,  5414,  5432,  5438,  5456,  5462,  5479,  5503,  5509,  5532,
    5543,  5554,  5565,  5576,  5587,  5598,  5609,  5620,  5631,  5642,
    5659,  5676,  5705,  5716,  5739,  5750,  5761,  5772,  5783,  5806,
    5829,  5853,  5859,  5877,  5883,  5894,  5929,  5952,  5975,  5986,
    5997,  6008,  6019,  6030,  6041,  6076,  6087,  6105,  6111,  6134,
    6158,  6164,  6175,  6192,  6215,  6226,  6237,  6248,  6265,  6276,
    6305,  6334,  6345,  6356,  6386,  6392,  6403,  6414,  6425,  6454,
    6483,  6494,  6505,  6528,  6539,  6556,  6579,  6590,  6601,  6631,
    6638,  6644,  6661,  6696,  6719,  6742,  6753,  6794,  6823,  6852,
    6869,  6886,  6903,  6920,  6937,  6954,  6971,  6988,  7005,  7022,
    7039,  7057,  7063,  7074,  7103,  7126,  7149,  7190,  7213,  7224,
    7235,  7246,  7275,  7304,  7321,  7338,  7367,  7384,  7401,  7412,
    7435
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "'~'", "';'", "'('", "'.'", "':'",
  "'\\''", "'+'", "'%'", "'`'", "'\"'", "'^'", "'*'", "'='", "'{'", "'$'",
  "'-'", "'<'", "','", "'?'", "'@'", "'>'", "'&'", "'/'", "'['", "'}'",
  "'!'", "']'", "'|'", "')'", "T_REQUIRE_ONCE", "T_REQUIRE", "T_EVAL",
  "T_INCLUDE_ONCE", "T_INCLUDE", "T_LOGICAL_OR", "T_LOGICAL_XOR",
  "T_LOGICAL_AND", "T_PRINT", "T_SR_EQUAL", "T_SL_EQUAL", "T_XOR_EQUAL",
  "T_OR_EQUAL", "T_AND_EQUAL", "T_MOD_EQUAL", "T_CONCAT_EQUAL",
  "T_DIV_EQUAL", "T_MUL_EQUAL", "T_MINUS_EQUAL", "T_PLUS_EQUAL",
  "T_BOOLEAN_OR", "T_BOOLEAN_AND", "T_IS_NOT_IDENTICAL", "T_IS_IDENTICAL",
  "T_IS_NOT_EQUAL", "T_IS_EQUAL", "T_IS_GREATER_OR_EQUAL",
  "T_IS_SMALLER_OR_EQUAL", "T_SR", "T_SL", "T_INSTANCEOF", "T_UNSET_CAST",
  "T_BOOL_CAST", "T_OBJECT_CAST", "T_ARRAY_CAST", "T_STRING_CAST",
  "T_DOUBLE_CAST", "T_INT_CAST", "T_DEC", "T_INC", "T_CLONE", "T_NEW",
  "T_EXIT", "T_IF", "T_ELSEIF", "T_ELSE", "T_ENDIF", "T_LNUMBER",
  "T_DNUMBER", "T_STRING", "T_STRING_VARNAME", "T_VARIABLE",
  "T_NUM_STRING", "T_INLINE_HTML", "T_CHARACTER", "T_BAD_CHARACTER",
  "T_ENCAPSED_AND_WHITESPACE", "T_CONSTANT_ENCAPSED_STRING", "T_ECHO",
  "T_DO", "T_WHILE", "T_ENDWHILE", "T_FOR", "T_ENDFOR", "T_FOREACH",
  "T_ENDFOREACH", "T_DECLARE", "T_ENDDECLARE", "T_AS", "T_SWITCH",
  "T_ENDSWITCH", "T_CASE", "T_DEFAULT", "T_BREAK", "T_CONTINUE",
  "T_FUNCTION", "T_CONST", "T_RETURN", "T_TRY", "T_CATCH", "T_THROW",
  "T_USE", "T_GLOBAL", "T_PUBLIC", "T_PROTECTED", "T_PRIVATE", "T_FINAL",
  "T_ABSTRACT", "T_STATIC", "T_VAR", "T_UNSET", "T_ISSET", "T_EMPTY",
  "T_HALT_COMPILER", "T_CLASS", "T_INTERFACE", "T_EXTENDS", "T_IMPLEMENTS",
  "T_OBJECT_OPERATOR", "T_DOUBLE_ARROW", "T_LIST", "T_ARRAY", "T_CLASS_C",
  "T_METHOD_C", "T_FUNC_C", "T_LINE", "T_FILE", "T_COMMENT",
  "T_DOC_COMMENT", "T_OPEN_TAG", "T_OPEN_TAG_WITH_ECHO", "T_CLOSE_TAG",
  "T_WHITESPACE", "T_START_HEREDOC", "T_END_HEREDOC",
  "T_DOLLAR_OPEN_CURLY_BRACES", "T_CURLY_OPEN", "T_PAAMAYIM_NEKUDOTAYIM",
  "$accept", "start", "top_statement_list", "top_statement",
  "inner_statement_list", "inner_statement", "stmt_end", "statement",
  "unticked_statement", "additional_catches",
  "non_empty_additional_catches", "additional_catch", "unset_variables",
  "unset_variable", "use_filename", "function_declaration_statement",
  "class_declaration_statement", "is_reference",
  "unticked_function_declaration_statement",
  "unticked_class_declaration_statement", "class_entry_type",
  "extends_from", "interface_entry", "interface_extends_list",
  "implements_list", "interface_list", "foreach_optional_arg",
  "foreach_variable", "for_statement", "foreach_statement",
  "declare_statement", "declare_list", "switch_case_list", "case_list",
  "case_separator", "while_statement", "elseif_list", "new_elseif_list",
  "else_single", "new_else_single", "parameter_list",
  "non_empty_parameter_list", "optional_class_type",
  "function_call_parameter_list", "non_empty_function_call_parameter_list",
  "global_var_list", "global_var", "static_var_list",
  "class_statement_list", "class_statement", "method_body",
  "variable_modifiers", "method_modifiers", "non_empty_member_modifiers",
  "member_modifier", "class_variable_declaration",
  "class_constant_declaration", "echo_expr_list", "for_expr",
  "non_empty_for_expr", "expr_without_variable", "function_call",
  "fully_qualified_class_name", "class_name_reference",
  "dynamic_class_name_reference", "dynamic_class_name_variable_properties",
  "dynamic_class_name_variable_property", "exit_expr", "ctor_arguments",
  "common_scalar", "static_scalar", "static_class_constant", "scalar",
  "static_array_pair_list", "possible_comma",
  "non_empty_static_array_pair_list", "expr", "r_variable", "w_variable",
  "rw_variable", "variable", "variable_properties", "variable_property",
  "method_or_not", "variable_without_objects", "static_member",
  "base_variable_with_function_calls", "base_variable",
  "reference_variable", "compound_variable", "dim_offset",
  "object_property", "object_dim_list", "variable_name",
  "simple_indirect_reference", "assignment_list",
  "assignment_list_element", "array_pair_list",
  "non_empty_array_pair_list", "encaps_list", "encaps_var",
  "encaps_var_offset", "internal_functions_in_yacc", "isset_variables",
  "class_constant", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   126,    59,    40,    46,    58,    39,    43,
      37,    96,    34,    94,    42,    61,   123,    36,    45,    60,
      44,    63,    64,    62,    38,    47,    91,   125,    33,    93,
     124,    41,   258,   259,   260,   261,   262,   263,   264,   265,
     266,   267,   268,   269,   270,   271,   272,   273,   274,   275,
     276,   277,   278,   279,   280,   281,   282,   283,   284,   285,
     286,   287,   288,   289,   290,   291,   292,   293,   294,   295,
     296,   297,   298,   299,   300,   301,   302,   303,   304,   305,
     306,   307,   308,   309,   310,   311,   312,   313,   314,   315,
     316,   317,   318,   319,   320,   321,   322,   323,   324,   325,
     326,   327,   328,   329,   330,   331,   332,   333,   334,   335,
     336,   337,   338,   339,   340,   341,   342,   343,   344,   345,
     346,   347,   348,   349,   350,   351,   352,   353,   354,   355,
     356,   357,   358,   359,   360,   361,   362,   363,   364,   365,
     366,   367,   368,   369,   370,   371,   372,   373,   374,   375
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,   150,   151,   152,   152,   153,   153,   153,   153,   154,
     154,   155,   155,   155,   155,   156,   156,   157,   158,   158,
     158,   158,   158,   158,   158,   158,   158,   158,   158,   158,
     158,   158,   158,   158,   158,   158,   158,   158,   158,   158,
     158,   158,   158,   158,   158,   159,   159,   160,   160,   161,
     162,   162,   163,   164,   164,   165,   166,   167,   167,   168,
     169,   169,   170,   170,   170,   171,   171,   172,   173,   173,
     174,   174,   175,   175,   176,   176,   177,   177,   178,   178,
     179,   179,   180,   180,   181,   181,   182,   182,   182,   182,
     183,   183,   183,   184,   184,   185,   185,   186,   186,   187,
     187,   188,   188,   189,   189,   190,   190,   191,   191,   191,
     191,   191,   191,   191,   191,   192,   192,   192,   193,   193,
     194,   194,   194,   194,   194,   194,   195,   195,   196,   196,
     196,   197,   197,   197,   197,   198,   198,   199,   199,   199,
     200,   200,   201,   201,   202,   202,   203,   203,   204,   204,
     204,   204,   204,   204,   205,   205,   205,   205,   206,   206,
     207,   207,   208,   208,   209,   209,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   210,   210,   210,   210,   210,   210,   210,   210,
     210,   210,   211,   211,   211,   211,   212,   213,   213,   214,
     214,   215,   215,   216,   217,   217,   217,   218,   218,   219,
     219,   219,   219,   219,   219,   219,   219,   220,   220,   220,
     220,   220,   220,   221,   222,   222,   222,   222,   222,   222,
     222,   223,   223,   224,   224,   225,   225,   225,   225,   226,
     226,   227,   228,   229,   230,   230,   231,   231,   232,   233,
     233,   234,   234,   235,   236,   236,   237,   237,   237,   238,
     238,   238,   239,   239,   240,   240,   241,   241,   242,   242,
     242,   243,   243,   244,   244,   245,   245,   246,   246,   246,
     247,   247,   248,   248,   248,   248,   248,   248,   248,   248,
     249,   249,   249,   249,   249,   249,   249,   249,   249,   249,
     249,   249,   250,   250,   250,   250,   250,   250,   251,   251,
     251,   252,   252,   252,   252,   252,   252,   252,   253,   253,
     254
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     1,     2,     0,     1,     1,     1,     4,     2,
       0,     1,     1,     1,     4,     1,     1,     1,     3,     7,
      10,     5,     7,     9,     5,     2,     3,     2,     3,     2,
       3,     3,     3,     3,     3,     1,     2,     3,     5,     8,
       8,     5,     1,    13,     3,     1,     0,     1,     2,     8,
       1,     3,     1,     1,     3,     1,     1,     0,     1,     9,
       7,     6,     1,     2,     2,     0,     2,     1,     0,     2,
       0,     2,     1,     3,     0,     2,     1,     2,     1,     4,
       1,     4,     1,     4,     3,     5,     3,     4,     4,     5,
       0,     5,     4,     1,     1,     1,     4,     0,     6,     0,
       7,     0,     2,     0,     3,     1,     0,     2,     3,     5,
       4,     4,     5,     7,     6,     0,     1,     1,     1,     0,
       1,     1,     2,     3,     3,     4,     3,     1,     1,     2,
       4,     3,     5,     1,     3,     2,     0,     3,     2,     8,
       1,     3,     1,     1,     0,     1,     1,     2,     1,     1,
       1,     1,     1,     1,     3,     5,     1,     3,     5,     4,
       3,     1,     0,     1,     3,     1,     6,     3,     4,     6,
       3,     2,     3,     3,     3,     3,     3,     3,     3,     3,
       3,     3,     3,     2,     2,     2,     2,     3,     3,     3,
       3,     3,     3,     3,     3,     3,     3,     3,     3,     3,
       3,     3,     3,     2,     2,     2,     2,     3,     3,     3,
       3,     3,     3,     3,     3,     3,     3,     5,     1,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     1,     4,
       3,     2,     4,     6,     6,     4,     1,     1,     1,     4,
       1,     2,     0,     2,     0,     2,     3,     0,     3,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     2,
       2,     4,     1,     3,     1,     1,     1,     1,     3,     3,
       3,     0,     2,     0,     1,     5,     3,     3,     1,     1,
       1,     1,     1,     1,     5,     1,     2,     0,     3,     3,
       0,     1,     2,     3,     1,     1,     1,     2,     1,     4,
       4,     1,     1,     4,     0,     1,     1,     1,     4,     4,
       1,     1,     3,     1,     2,     3,     1,     1,     4,     0,
       0,     2,     5,     3,     3,     1,     6,     4,     4,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     0,     1,     4,     3,     3,     6,     3,     1,     1,
       1,     4,     4,     2,     2,     4,     2,     2,     1,     3,
       3
};

/* YYDEFACT[STATE-NAME] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint16 yydefact[] =
{
       4,     0,     2,     1,     0,    16,     0,   341,     0,   341,
     341,    10,   313,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   244,     0,   249,   250,   264,   265,   302,
      35,   251,     0,     0,     0,     0,     0,     0,     0,     0,
       0,    57,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    62,    67,     0,     0,   254,   255,
     256,   252,   253,    15,   341,     3,    42,     5,    17,     6,
       7,    55,    56,     0,     0,   280,   295,     0,   267,   228,
       0,   279,     0,   281,     0,   298,   285,   294,   296,   301,
       0,   218,   266,   206,     0,     0,   203,     0,     0,     0,
       0,   204,   227,   205,   357,   356,     0,   354,   353,   231,
     225,   224,   223,   222,   221,   220,   219,   236,     0,   186,
     283,   184,   171,   237,     0,   247,   238,   240,   296,     0,
       0,   226,     0,   119,     0,   161,     0,     0,   162,     0,
       0,     0,    25,     0,    27,     0,    58,     0,    29,   280,
       0,   281,    10,     0,     0,    53,     0,     0,   128,     0,
     127,    64,    63,   133,     0,     0,     0,     0,     0,   319,
     320,     0,    65,    68,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,    36,   185,   183,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,   119,     0,     0,   304,
     314,   297,   216,   269,   338,   336,   339,   337,   331,   342,
     332,   334,   335,   333,   340,     0,     0,   330,   230,   268,
      18,     0,     9,    11,    12,    13,     0,     0,     0,     0,
     119,   170,     0,   297,   245,     0,     0,     0,     0,   118,
     280,   281,     0,    34,     0,     0,     0,   163,   165,   280,
     281,     0,     0,     0,    26,    28,     0,    30,    31,     0,
      44,     0,    37,     0,   129,   281,     0,    32,     0,     0,
      33,     0,    50,    52,   358,     0,     0,     0,     0,   317,
       0,   316,     0,   325,     0,   273,   270,     0,    70,     0,
       0,   360,   293,   291,     0,   195,   196,   200,   194,   198,
     197,   211,     0,   213,   193,   199,   192,   189,   191,   190,
     187,   188,   208,   207,   210,   209,   214,   212,   202,   201,
     215,     0,   167,   182,   181,   180,   179,   178,   177,   176,
     175,   174,   173,   172,     0,     0,   311,   307,   290,   306,
     310,     0,   305,     0,     0,     0,   265,     0,     0,     0,
     303,   355,     0,   293,     0,   242,   246,     0,   122,   282,
     232,     0,   160,     0,     0,   162,     0,     0,     0,     0,
       0,     0,     0,   115,     0,    54,     0,   126,     0,     0,
     258,     0,   257,   134,   262,   131,     0,     0,     0,   351,
     352,     8,   319,   319,     0,   329,     0,   229,   274,   321,
     236,    66,     0,     0,    69,    72,   136,   119,   119,   292,
       0,     0,   168,   235,     0,   119,   287,     0,   304,   300,
     299,   348,   350,   349,     0,   344,     0,   345,   347,     0,
     248,   239,    10,    97,     0,   280,   281,     0,    10,    95,
      21,     0,   164,    74,     0,    74,    76,    84,     0,    10,
      82,    41,    90,    90,    24,   116,   117,     0,   105,     0,
       0,   130,   259,   260,     0,   271,     0,    51,    38,   359,
       0,   315,     0,     0,   324,     0,   323,    71,   136,     0,
     144,     0,     0,   217,   247,   312,     0,   284,     0,     0,
     343,     0,    14,     0,   241,    99,   101,   125,     0,     0,
     162,     0,     0,    77,     0,     0,     0,    90,     0,    90,
       0,     0,   115,     0,   107,     0,   263,   278,     0,   273,
     132,   318,   166,   328,   327,     0,   144,    73,    61,     0,
     148,   149,   150,   153,   152,   151,   143,   135,     0,     0,
     142,   146,     0,   233,   234,   169,   289,     0,   286,   309,
     308,     0,   243,   103,     0,     0,    19,    22,     0,     0,
      75,     0,     0,    85,     0,     0,     0,     0,     0,     0,
      86,    10,     0,   108,     0,     0,     0,   261,   274,   272,
       0,   322,    60,     0,   156,     0,    57,   147,     0,   138,
     290,   346,     0,     0,     0,     0,   102,    96,     0,    10,
      80,    40,    39,    83,     0,    88,     0,    94,    93,    10,
      87,     0,     0,   111,     0,   110,     0,   277,   276,   326,
       0,     0,     0,   137,     0,     0,   288,     0,    10,     0,
       0,    10,    78,    23,     0,    89,    10,    92,    59,   112,
       0,   109,     0,     0,   159,   157,   154,     0,     0,     0,
     104,    20,     0,     0,     0,    91,     0,   114,    10,   275,
       0,   115,   158,     0,    98,     0,    81,   113,     0,   155,
       0,    10,    79,    46,     0,   100,     0,    43,    45,    47,
      10,   140,   139,     0,    48,     0,     0,   141,     0,     0,
      10,     0,    49
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     1,     2,    75,   109,   252,    76,   253,    78,   707,
     708,   709,   301,   302,   166,   254,   255,   157,    81,    82,
      83,   318,    84,   320,   433,   434,   532,   475,   663,   631,
     481,   282,   484,   538,   639,   470,   526,   583,   586,   624,
     487,   488,   489,   268,   269,   169,   170,   174,   510,   567,
     712,   568,   569,   570,   571,   615,   572,   144,   276,   277,
      85,    86,    87,   135,   136,   461,   524,   141,   261,    88,
     413,   414,    89,   548,   429,   549,    90,    91,   388,    92,
      93,   517,   578,   446,    94,    95,    96,    97,    98,    99,
     373,   368,   369,   370,   100,   310,   311,   314,   315,   105,
     247,   454,   101,   305,   102
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -389
static const yytype_int16 yypact[] =
{
    -389,    66,  2440,  -389,  4349,  -389,  4349,  -389,  4349,  -389,
    -389,  -389,    27,  4349,  4349,  4349,  4349,  4349,    40,  4349,
    4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,    62,
      62,  4349,    89,    57,    65,  -389,  -389,    12,  -389,  -389,
    -389,  -389,  4349,  3441,   137,   139,   169,   178,   190,  3584,
    3584,    58,  3584,   164,  4349,    25,    36,    13,    75,   133,
     198,   213,   221,   237,  -389,  -389,   240,   255,  -389,  -389,
    -389,  -389,  -389,  -389,  -389,  -389,  -389,  -389,  -389,  -389,
    -389,  -389,  -389,   181,   193,  -389,  -389,   121,  -389,  -389,
     718,  -389,   107,   890,   272,  -389,   148,  -389,    55,  -389,
      39,  -389,  -389,  -389,  4740,    49,    38,   289,   437,   884,
    4349,    38,  -389,   220,  5709,  5709,  4349,  5709,  5709,  5880,
    -389,  -389,  -389,  -389,  -389,  -389,  -389,   275,   136,  -389,
    -389,  -389,  -389,   140,   152,   299,  -389,   177,    28,    39,
    3669,  -389,  4349,  3754,    14,  5709,   214,  4349,  4349,  4349,
     230,  4349,  -389,   718,  -389,   718,  -389,   232,  -389,    24,
    5709,   559,  -389,   718,   225,  -389,    24,    34,  -389,    16,
    -389,  -389,  -389,   302,    17,    62,    62,    62,   292,     2,
    3839,   663,   192,   196,    92,  4349,  4349,  4349,  4349,  4349,
    4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,
    4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,  4349,
      89,  -389,  -389,  -389,  3924,  4349,  4349,  4349,  4349,  4349,
    4349,  4349,  4349,  4349,  4349,  4349,  3754,    88,  4349,  4349,
      27,    68,  -389,  -389,  -389,  -389,  -389,  -389,  -389,    23,
    -389,  -389,  -389,  -389,  -389,  4434,    62,  -389,  -389,  -389,
    -389,   323,  -389,  -389,  -389,  -389,  4797,  4854,   123,    42,
    3754,  -389,    88,    28,  -389,  4911,  4968,    62,   295,   313,
      60,  1068,  4349,  -389,   329,  5025,   332,   318,  5709,   242,
    1008,   328,    77,  5082,  -389,  -389,   342,  -389,  -389,  1153,
    -389,   317,  -389,  4349,  -389,  -389,    36,  -389,   490,   267,
    -389,   167,  -389,  -389,  -389,   201,   322,    24,   347,  -389,
     216,  -389,    62,  4567,   325,   337,  -389,   277,   249,   277,
     343,   366,   379,    28,    39,    38,    38,   220,  6093,   220,
      38,   321,  5139,   321,  6150,   220,  6073,  5766,  5823,  5880,
    5994,  6016,  6168,  6168,  6168,  6168,   321,   321,   219,   219,
    -389,   175,  5880,  5880,  5880,  5880,  5880,  5880,  5880,  5880,
    5880,  5880,  5880,  5880,   354,  4349,  -389,  -389,   381,    48,
    -389,  5196,  5709,   359,   203,   310,   369,  5253,   365,   367,
    -389,  -389,   366,  -389,   368,  -389,  -389,  2726,  -389,  -389,
    -389,  4009,  5709,  4349,  2869,  4349,  4349,    62,   176,   490,
     315,  3012,   259,    -4,   297,  -389,  5310,  -389,   490,   490,
     261,   406,  -389,  -389,  -389,   399,    62,    24,    62,  -389,
    -389,  -389,     2,     2,   400,  -389,  4094,  -389,  4179,  -389,
    -389,  -389,   277,   401,   398,  -389,  -389,  3754,  3754,    28,
    4349,    89,  -389,  -389,  5367,  3754,  -389,  4349,  4349,  -389,
    -389,  -389,  -389,  -389,   392,  -389,  4349,  -389,  -389,    24,
    -389,   293,  -389,  -389,    62,   241,  1302,  5424,  -389,  -389,
    -389,   420,  5709,   298,    62,   298,  -389,  -389,   411,  -389,
    -389,  -389,   424,   426,  -389,  -389,  -389,   402,   414,     5,
     433,  -389,  -389,  -389,   358,   490,   490,  -389,  -389,  -389,
     248,  -389,  4349,    62,  5709,    62,  4624,   398,  -389,   277,
      94,   409,   412,  5937,   299,  -389,   413,   320,  5481,   419,
    -389,  5538,  -389,    88,  -389,  2583,   146,  -389,    24,  1296,
    4349,   176,   425,  -389,   427,   490,  1439,  -389,   189,  -389,
      -5,   439,     8,   374,   444,   277,  -389,   330,   429,   442,
    -389,  -389,  5880,  -389,  -389,  4264,   246,  -389,  -389,   384,
    -389,  -389,  -389,  -389,  -389,  -389,  -389,  -389,   385,   360,
     134,  -389,    20,  -389,  -389,  -389,  -389,    88,  -389,  -389,
    -389,   443,  -389,   218,   466,  3441,  -389,  -389,    24,   441,
    -389,  3155,  3155,  -389,    24,   195,    24,  4349,   120,    -1,
    -389,  -389,    45,   460,   490,   394,   490,  -389,   490,  -389,
      62,  5709,  -389,   463,   467,    21,    58,  -389,   403,  -389,
     381,  -389,   476,   479,   410,  4349,  -389,  -389,  3298,  -389,
    -389,  -389,  -389,  -389,    24,  -389,  4683,  -389,  -389,  -389,
    -389,  1582,   404,   474,   490,  -389,   459,  -389,   361,  -389,
     490,   490,   408,  -389,   415,   478,  -389,  4349,  -389,    24,
    5595,  -389,  -389,  -389,  1725,  -389,  -389,  2583,  -389,   482,
     490,  -389,   484,   490,  -389,  -389,   488,   489,   490,  5652,
    2583,  -389,  3441,  1868,    24,  2583,   490,  -389,  -389,  -389,
     490,    -4,  -389,   498,  -389,    24,  -389,  -389,  2011,  -389,
     480,  -389,  -389,   396,    19,  2583,   504,  -389,   396,  -389,
    -389,  -389,  -389,   277,  -389,  2154,   430,  -389,   486,   494,
    -389,  2297,  -389
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
    -389,  -389,  -389,  -389,  -160,  -389,   -43,     3,  -389,  -389,
    -389,  -193,  -389,   103,  -389,   520,   524,   -89,  -389,  -389,
    -389,  -389,  -389,  -389,  -389,    98,    56,     1,  -389,   -59,
    -389,  -389,  -389,  -388,  -102,  -389,  -389,  -389,  -389,  -389,
    -156,  -389,    -6,  -218,  -389,  -389,   247,  -389,    29,  -389,
    -389,  -389,  -389,  -389,   -26,  -389,  -389,  -389,  -362,  -389,
     -36,  -389,   -29,  -195,  -389,  -389,  -389,  -389,    32,    43,
    -341,  -389,  -389,  -389,    -7,  -389,   649,   380,  -298,    63,
       9,  -389,  -389,   -72,  -172,  -389,  -389,   -21,   -28,  -389,
     104,  -252,  -389,  -389,   -19,   132,   138,  -389,  -389,    22,
    -389,  -389,  -389,  -389,  -389
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -293
static const yytype_int16 yytable[] =
{
     128,   128,   289,   134,   138,    77,   152,   154,   364,   158,
     385,   137,   322,   139,   425,   350,   159,   143,     5,    12,
       5,     5,   600,     5,     5,     5,   640,  -106,     5,   543,
     164,   107,   108,   471,   272,   710,   296,   299,   130,   130,
     618,   652,   384,   110,   228,   116,   146,   211,   187,   374,
     293,    12,   189,   167,   229,   367,   230,   233,   477,    12,
    -291,   161,   140,   195,   447,   234,     3,   492,   493,   642,
     142,   228,   231,  -292,   448,   235,   236,   485,   237,    12,
    -120,   229,   156,   127,   228,    39,   322,   383,   544,   485,
     367,  -120,   129,   131,   229,   540,   181,   400,   597,   598,
     210,   273,   597,   598,   365,    12,    12,   270,   401,    12,
     284,   263,   285,   279,   165,   127,   287,    39,   288,   168,
     290,   558,    39,   292,   637,    39,   297,   638,   643,   486,
     238,   300,   239,   240,   308,   241,   242,   243,   128,   171,
      12,   486,   147,   127,   148,    39,   128,   128,   128,   595,
     128,   599,   271,   375,   547,   550,   323,    73,   280,    73,
      73,  -236,    73,    73,    73,   324,   527,    73,   589,   366,
     133,    39,    39,   321,   149,    39,   295,   212,   213,   244,
     162,   134,   138,   150,   303,   304,   306,   416,   309,   137,
     270,   139,    12,    12,   593,   151,   245,   246,   417,   323,
     474,   172,   559,   175,   382,   553,    39,   554,   324,   560,
     561,   562,   563,   564,   565,   566,   173,   128,   176,   511,
     512,   418,   584,   585,   270,   185,   177,   516,   186,   187,
     323,   323,   419,   189,   323,   271,   423,   190,   128,   324,
     324,  -145,   178,   324,   195,   179,   514,   424,   441,   560,
     561,   562,   563,   564,   565,   378,   127,   127,    39,    39,
     180,  -123,   182,   645,   421,   647,   482,   648,   423,   271,
     184,   582,  -123,   612,   183,   483,   389,   226,   227,   551,
     143,   210,   210,   128,   451,   258,   452,   453,   431,  -236,
     435,   596,   597,   598,   622,   623,   439,   634,   597,   598,
     248,   259,   525,   671,   260,   234,   274,   262,   529,   674,
     675,   281,   649,   286,   291,   235,   236,   298,   237,   536,
     317,   389,   128,   307,   319,   620,   390,   185,   379,   687,
     186,   187,   689,   391,   393,   189,   395,   692,   396,   190,
    -293,   412,   397,   399,  -293,   697,   195,   403,   405,   699,
     415,   367,   422,   420,   559,   465,   427,   428,   430,   436,
     442,   560,   561,   562,   563,   564,   565,   566,   128,   128,
     238,   437,   239,   240,   498,   241,   242,   243,   432,  -293,
    -293,   208,   209,   210,   438,   443,   445,   128,   450,   128,
     463,   455,   458,   128,   128,   456,   478,   469,   459,   460,
     466,   270,   270,   435,   480,   367,   473,   476,   490,   270,
     494,   495,   134,   138,   496,   502,   522,   508,   509,   244,
     137,   520,   139,   523,   530,   303,   535,   499,   537,   531,
     539,   309,   309,   541,   542,   128,   245,   246,   545,   546,
     573,   641,   412,   574,   576,   128,   271,   271,   580,   249,
     577,   412,   412,   234,   271,   601,   591,   603,   592,   604,
     607,   606,   608,   235,   236,   613,   237,   616,   614,   664,
     621,   625,   628,   389,   128,   644,   128,   646,   650,   667,
     557,   657,   651,   533,   655,   587,   658,   669,   659,   670,
     672,   676,   673,   678,   691,   323,   677,   686,   680,   408,
     688,   683,   128,   690,   324,   701,   685,   706,   409,   713,
     720,   704,   389,   718,   389,   714,   605,   719,   238,   497,
     239,   240,    79,   241,   242,   243,    80,   654,   698,   619,
     507,   534,   590,   632,   666,   700,   602,   556,   412,   412,
     476,   705,   609,   407,   617,   627,   575,   294,   656,   323,
     715,   633,   519,   635,   500,     0,     0,     0,   324,     0,
     721,   501,     0,     5,     0,     0,     0,   244,     0,    35,
      36,   410,   653,     0,   214,     0,     0,     0,   412,    41,
       0,   128,     0,     0,   245,   246,     0,     0,   626,     0,
       0,   665,     0,     0,   630,   630,     0,     0,     0,     0,
     215,   216,   217,   218,   219,   220,   221,   222,   223,   224,
     225,     0,     0,     0,     0,     0,   681,     0,     0,   389,
       0,     0,     0,   411,    68,    69,    70,    71,    72,  -283,
    -283,   662,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   696,     0,     0,     0,     0,     0,   412,     0,   412,
       0,   412,   702,   103,     0,   104,     0,   106,     0,     0,
       0,   711,   111,   112,   113,   114,   115,     0,   117,   118,
     119,   120,   121,   122,   123,   124,   125,   126,     0,   234,
     132,     0,     0,     0,   716,   694,     0,   412,     0,   235,
     236,   145,   237,   412,   412,     0,     0,     0,   153,   155,
       0,   160,    73,   163,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   412,     0,     0,   412,     0,     0,     0,
       0,   412,     5,     0,   185,     0,     0,   186,   187,   412,
       0,   188,   189,   412,     0,     0,   190,   191,     0,   192,
       0,   193,   194,   195,   238,     0,   239,   240,   196,   241,
     242,   243,     0,     0,     0,   197,   198,   199,     0,   256,
       0,     0,     0,     0,     0,   257,     0,     0,     0,     0,
     200,   201,   202,   203,   204,   205,   206,   207,   208,   209,
     210,     0,     0,     0,     0,     0,     0,     0,     0,   265,
       0,   266,   160,   244,     0,     0,   275,   278,   160,     0,
     283,     0,     0,     0,     0,     0,     0,     0,     0,   316,
     245,   246,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   313,
       0,     0,     0,     0,   325,   326,   327,   328,   329,   330,
     331,   332,   333,   334,   335,   336,   337,   338,   339,   340,
     341,   342,   343,   344,   345,   346,   347,   348,   349,     0,
       0,    73,     0,   352,   353,   354,   355,   356,   357,   358,
     359,   360,   361,   362,   363,   160,     0,   371,   372,     0,
       0,     0,     0,     0,     0,     0,     0,     4,     5,     6,
       0,     0,     7,     8,   377,     9,    10,     0,     0,     0,
      11,    12,    13,     0,     0,   214,    14,     0,     0,   160,
       0,   250,    15,     0,     0,     0,    16,    17,    18,    19,
      20,   392,     0,     0,    21,     0,     0,     0,     0,     0,
       0,   215,   216,   217,   218,   219,   220,   221,   222,   223,
     224,   225,   406,     0,     0,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
    -283,  -283,     0,    35,    36,    37,    38,    39,     0,    40,
       0,     0,     0,    41,    42,    43,    44,     0,    45,     0,
      46,     0,    47,     0,     0,    48,     0,     0,     0,    49,
      50,    51,     0,    52,    53,     0,    54,    55,    56,     0,
       0,     0,    57,    58,    59,     0,    60,    61,    62,   251,
      64,    65,     0,     0,   444,     0,    66,    67,    68,    69,
      70,    71,    72,   214,     0,     0,     0,    73,     0,    74,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     160,     0,   467,     0,   278,   472,     0,     0,     0,   215,
     216,   217,   218,   219,   220,   221,   222,   223,   224,   225,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   504,     0,   506,  -283,  -283,
       0,     0,     0,   214,     0,     0,   160,   160,  -121,   513,
       0,     0,     0,     0,   160,     0,   518,   372,     0,  -121,
       0,     0,     0,     0,     0,   521,     0,     0,   398,   215,
     216,   217,   218,   219,   220,   221,   222,   223,   224,   225,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,  -283,  -283,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   552,     0,     0,     0,     0,     4,     5,     6,     0,
       0,     7,     8,     0,     9,    10,     0,     0,     0,    11,
      12,    13,     0,     0,     0,    14,     0,     0,     0,   278,
     404,    15,     0,     0,     0,    16,    17,    18,    19,    20,
       0,     0,     0,    21,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   611,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,    22,    23,    24,    25,
      26,    27,    28,    29,    30,    31,    32,    33,    34,     0,
       0,     0,    35,    36,    37,    38,    39,     0,    40,     0,
       0,     0,    41,    42,    43,    44,   636,    45,     0,    46,
       0,    47,     0,     0,    48,     0,     0,     0,    49,    50,
      51,     0,    52,    53,     0,    54,    55,    56,     0,     0,
       0,    57,    58,    59,   660,    60,    61,    62,   251,    64,
      65,     0,     0,     0,     0,    66,    67,    68,    69,    70,
      71,    72,     0,     0,     0,     0,    73,     0,    74,     4,
       5,     6,     0,     0,     7,     8,   679,     9,    10,     0,
       0,     0,    11,    12,    13,     0,     0,   214,    14,     0,
       0,     0,  -124,     0,    15,     0,     0,     0,    16,    17,
      18,    19,    20,  -124,     0,     0,    21,     0,     0,     0,
       0,     0,     0,   215,   216,   217,   218,   219,   220,   221,
     222,   223,   224,   225,     0,     0,     0,     0,     0,    22,
      23,    24,    25,    26,    27,    28,    29,    30,    31,    32,
      33,    34,  -283,  -283,     0,    35,    36,    37,    38,    39,
       0,    40,     0,     0,     0,    41,    42,    43,    44,   588,
      45,     0,    46,     0,    47,     0,     0,    48,     0,     0,
       0,    49,    50,    51,     0,    52,    53,     0,    54,    55,
      56,     0,     0,     0,    57,    58,    59,     0,    60,    61,
      62,   251,    64,    65,     0,     0,     0,     0,    66,    67,
      68,    69,    70,    71,    72,     0,     0,     0,     0,    73,
       0,    74,     4,     5,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,    11,    12,    13,     0,     0,
       0,    14,     0,     0,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,     0,     0,    21,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,    34,     0,     0,     0,    35,    36,
      37,    38,    39,     0,    40,     0,     0,     0,    41,    42,
      43,    44,     0,    45,     0,    46,     0,    47,   594,     0,
      48,     0,     0,     0,    49,    50,    51,     0,    52,    53,
       0,    54,    55,    56,     0,     0,     0,    57,    58,    59,
       0,    60,    61,    62,   251,    64,    65,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,    73,     0,    74,     4,     5,     6,     0,     0,
       7,     8,     0,     9,    10,     0,     0,     0,    11,    12,
      13,     0,     0,     0,    14,     0,     0,     0,     0,   668,
      15,     0,     0,     0,    16,    17,    18,    19,    20,     0,
       0,     0,    21,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,    22,    23,    24,    25,    26,
      27,    28,    29,    30,    31,    32,    33,    34,     0,     0,
       0,    35,    36,    37,    38,    39,     0,    40,     0,     0,
       0,    41,    42,    43,    44,     0,    45,     0,    46,     0,
      47,     0,     0,    48,     0,     0,     0,    49,    50,    51,
       0,    52,    53,     0,    54,    55,    56,     0,     0,     0,
      57,    58,    59,     0,    60,    61,    62,   251,    64,    65,
       0,     0,     0,     0,    66,    67,    68,    69,    70,    71,
      72,     0,     0,     0,     0,    73,     0,    74,     4,     5,
       6,     0,     0,     7,     8,     0,     9,    10,     0,     0,
       0,    11,    12,    13,     0,     0,     0,    14,     0,     0,
       0,     0,     0,    15,     0,     0,     0,    16,    17,    18,
      19,    20,     0,     0,     0,    21,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,    22,    23,
      24,    25,    26,    27,    28,    29,    30,    31,    32,    33,
      34,     0,     0,     0,    35,    36,    37,    38,    39,     0,
      40,     0,     0,     0,    41,    42,    43,    44,     0,    45,
       0,    46,   684,    47,     0,     0,    48,     0,     0,     0,
      49,    50,    51,     0,    52,    53,     0,    54,    55,    56,
       0,     0,     0,    57,    58,    59,     0,    60,    61,    62,
     251,    64,    65,     0,     0,     0,     0,    66,    67,    68,
      69,    70,    71,    72,     0,     0,     0,     0,    73,     0,
      74,     4,     5,     6,     0,     0,     7,     8,     0,     9,
      10,     0,     0,     0,    11,    12,    13,     0,     0,     0,
      14,     0,     0,     0,     0,     0,    15,     0,     0,     0,
      16,    17,    18,    19,    20,     0,     0,     0,    21,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,    22,    23,    24,    25,    26,    27,    28,    29,    30,
      31,    32,    33,    34,     0,     0,     0,    35,    36,    37,
      38,    39,     0,    40,     0,     0,     0,    41,    42,    43,
      44,     0,    45,   695,    46,     0,    47,     0,     0,    48,
       0,     0,     0,    49,    50,    51,     0,    52,    53,     0,
      54,    55,    56,     0,     0,     0,    57,    58,    59,     0,
      60,    61,    62,   251,    64,    65,     0,     0,     0,     0,
      66,    67,    68,    69,    70,    71,    72,     0,     0,     0,
       0,    73,     0,    74,     4,     5,     6,     0,     0,     7,
       8,     0,     9,    10,     0,     0,     0,    11,    12,    13,
       0,     0,     0,    14,     0,     0,     0,     0,   703,    15,
       0,     0,     0,    16,    17,    18,    19,    20,     0,     0,
       0,    21,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    22,    23,    24,    25,    26,    27,
      28,    29,    30,    31,    32,    33,    34,     0,     0,     0,
      35,    36,    37,    38,    39,     0,    40,     0,     0,     0,
      41,    42,    43,    44,     0,    45,     0,    46,     0,    47,
       0,     0,    48,     0,     0,     0,    49,    50,    51,     0,
      52,    53,     0,    54,    55,    56,     0,     0,     0,    57,
      58,    59,     0,    60,    61,    62,   251,    64,    65,     0,
       0,     0,     0,    66,    67,    68,    69,    70,    71,    72,
       0,     0,     0,     0,    73,     0,    74,     4,     5,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
      11,    12,    13,     0,     0,     0,    14,     0,     0,     0,
       0,   717,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,     0,     0,    21,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
       0,     0,     0,    35,    36,    37,    38,    39,     0,    40,
       0,     0,     0,    41,    42,    43,    44,     0,    45,     0,
      46,     0,    47,     0,     0,    48,     0,     0,     0,    49,
      50,    51,     0,    52,    53,     0,    54,    55,    56,     0,
       0,     0,    57,    58,    59,     0,    60,    61,    62,   251,
      64,    65,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,    73,     0,    74,
       4,     5,     6,     0,     0,     7,     8,     0,     9,    10,
       0,     0,     0,    11,    12,    13,     0,     0,     0,    14,
       0,     0,     0,     0,   722,    15,     0,     0,     0,    16,
      17,    18,    19,    20,     0,     0,     0,    21,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
      22,    23,    24,    25,    26,    27,    28,    29,    30,    31,
      32,    33,    34,     0,     0,     0,    35,    36,    37,    38,
      39,     0,    40,     0,     0,     0,    41,    42,    43,    44,
       0,    45,     0,    46,     0,    47,     0,     0,    48,     0,
       0,     0,    49,    50,    51,     0,    52,    53,     0,    54,
      55,    56,     0,     0,     0,    57,    58,    59,     0,    60,
      61,    62,   251,    64,    65,     0,     0,     0,     0,    66,
      67,    68,    69,    70,    71,    72,     0,     0,     0,     0,
      73,     0,    74,     4,     5,     6,     0,     0,     7,     8,
       0,     9,    10,     0,     0,     0,    11,    12,    13,     0,
       0,     0,    14,     0,     0,     0,     0,     0,    15,     0,
       0,     0,    16,    17,    18,    19,    20,     0,     0,     0,
      21,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,    22,    23,    24,    25,    26,    27,    28,
      29,    30,    31,    32,    33,    34,     0,     0,     0,    35,
      36,    37,    38,    39,     0,    40,     0,     0,     0,    41,
      42,    43,    44,     0,    45,     0,    46,     0,    47,     0,
       0,    48,     0,     0,     0,    49,    50,    51,     0,    52,
      53,     0,    54,    55,    56,     0,     0,     0,    57,    58,
      59,     0,    60,    61,    62,    63,    64,    65,     0,     0,
       0,     0,    66,    67,    68,    69,    70,    71,    72,     0,
       0,     0,     0,    73,     0,    74,     4,     5,     6,     0,
       0,     7,     8,     0,     9,    10,     0,     0,     0,    11,
      12,    13,     0,     0,     0,    14,     0,     0,     0,     0,
       0,    15,     0,     0,     0,    16,    17,    18,    19,    20,
       0,     0,     0,    21,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,    22,    23,    24,    25,
      26,    27,    28,    29,    30,    31,    32,    33,    34,     0,
       0,     0,    35,    36,    37,    38,    39,     0,    40,     0,
       0,     0,    41,    42,    43,    44,     0,    45,     0,    46,
       0,    47,     0,     0,    48,     0,     0,     0,    49,    50,
      51,     0,    52,    53,     0,    54,    55,    56,     0,     0,
       0,    57,    58,    59,     0,    60,    61,    62,   251,    64,
      65,     0,     0,     0,     0,    66,    67,    68,    69,    70,
      71,    72,     0,     0,     0,     0,    73,     0,    74,     4,
       5,     6,     0,   462,     7,     8,     0,     9,    10,     0,
       0,     0,    11,    12,    13,     0,     0,     0,    14,     0,
       0,     0,     0,     0,    15,     0,     0,     0,    16,    17,
      18,    19,    20,     0,     0,     0,    21,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,    22,
      23,    24,    25,    26,    27,    28,    29,    30,    31,    32,
      33,    34,     0,     0,     0,    35,    36,    37,    38,    39,
       0,    40,     0,     0,     0,    41,    42,    43,    44,     0,
      45,     0,    46,     0,    47,     0,     0,    48,     0,     0,
       0,    49,    50,     0,     0,    52,    53,     0,    54,    55,
      56,     0,     0,     0,     0,     0,    59,     0,    60,    61,
      62,     0,     0,     0,     0,     0,     0,     0,    66,    67,
      68,    69,    70,    71,    72,     0,     0,     0,     0,    73,
       0,    74,     4,     5,     6,     0,   468,     7,     8,     0,
       9,    10,     0,     0,     0,    11,    12,    13,     0,     0,
       0,    14,     0,     0,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,     0,     0,    21,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,    34,     0,     0,     0,    35,    36,
      37,    38,    39,     0,    40,     0,     0,     0,    41,    42,
      43,    44,     0,    45,     0,    46,     0,    47,     0,     0,
      48,     0,     0,     0,    49,    50,     0,     0,    52,    53,
       0,    54,    55,    56,     0,     0,     0,     0,     0,    59,
       0,    60,    61,    62,     0,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,    73,     0,    74,     4,     5,     6,     0,   479,
       7,     8,     0,     9,    10,     0,     0,     0,    11,    12,
      13,     0,     0,     0,    14,     0,     0,     0,     0,     0,
      15,     0,     0,     0,    16,    17,    18,    19,    20,     0,
       0,     0,    21,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,    22,    23,    24,    25,    26,
      27,    28,    29,    30,    31,    32,    33,    34,     0,     0,
       0,    35,    36,    37,    38,    39,     0,    40,     0,     0,
       0,    41,    42,    43,    44,     0,    45,     0,    46,     0,
      47,     0,     0,    48,     0,     0,     0,    49,    50,     0,
       0,    52,    53,     0,    54,    55,    56,     0,     0,     0,
       0,     0,    59,     0,    60,    61,    62,     0,     0,     0,
       0,     0,     0,     0,    66,    67,    68,    69,    70,    71,
      72,     0,     0,     0,     0,    73,     0,    74,     4,     5,
       6,     0,   629,     7,     8,     0,     9,    10,     0,     0,
       0,    11,    12,    13,     0,     0,     0,    14,     0,     0,
       0,     0,     0,    15,     0,     0,     0,    16,    17,    18,
      19,    20,     0,     0,     0,    21,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,    22,    23,
      24,    25,    26,    27,    28,    29,    30,    31,    32,    33,
      34,     0,     0,     0,    35,    36,    37,    38,    39,     0,
      40,     0,     0,     0,    41,    42,    43,    44,     0,    45,
       0,    46,     0,    47,     0,     0,    48,     0,     0,     0,
      49,    50,     0,     0,    52,    53,     0,    54,    55,    56,
       0,     0,     0,     0,     0,    59,     0,    60,    61,    62,
       0,     0,     0,     0,     0,     0,     0,    66,    67,    68,
      69,    70,    71,    72,     0,     0,     0,     0,    73,     0,
      74,     4,     5,     6,     0,   661,     7,     8,     0,     9,
      10,     0,     0,     0,    11,    12,    13,     0,     0,     0,
      14,     0,     0,     0,     0,     0,    15,     0,     0,     0,
      16,    17,    18,    19,    20,     0,     0,     0,    21,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,    22,    23,    24,    25,    26,    27,    28,    29,    30,
      31,    32,    33,    34,     0,     0,     0,    35,    36,    37,
      38,    39,     0,    40,     0,     0,     0,    41,    42,    43,
      44,     0,    45,     0,    46,     0,    47,     0,     0,    48,
       0,     0,     0,    49,    50,     0,     0,    52,    53,     0,
      54,    55,    56,     0,     0,     0,     0,     0,    59,     0,
      60,    61,    62,     0,     0,     0,     0,     0,     0,     0,
      66,    67,    68,    69,    70,    71,    72,     0,     0,     0,
       0,    73,     0,    74,     4,     5,     6,     0,     0,     7,
       8,     0,     9,    10,     0,     0,     0,    11,    12,    13,
       0,     0,     0,    14,     0,     0,     0,     0,     0,    15,
       0,     0,     0,    16,    17,    18,    19,    20,     0,     0,
       0,    21,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    22,    23,    24,    25,    26,    27,
      28,    29,    30,    31,    32,    33,    34,     0,     0,     0,
      35,    36,    37,    38,    39,     0,    40,     0,     0,     0,
      41,    42,    43,    44,     0,    45,     0,    46,     0,    47,
       0,     0,    48,     0,     0,     0,    49,    50,     0,     0,
      52,    53,     0,    54,    55,    56,     0,     0,     0,     0,
       0,    59,     0,    60,    61,    62,     0,     0,     0,     0,
       0,     0,     0,    66,    67,    68,    69,    70,    71,    72,
       0,     0,     0,     0,    73,     0,    74,     4,     5,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,     0,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,     0,     0,    21,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,    38,    39,     0,     0,
       0,     0,     4,    41,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,     0,    12,    13,     0,     0,
       0,    14,     0,     0,     0,     0,     0,    15,     0,     0,
     264,    16,    17,    18,    19,    20,     0,    61,    62,    21,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,    73,     0,    74,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,     0,     0,     0,     0,    35,    36,
      37,    38,    39,     0,     0,     0,     0,     4,    41,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,   267,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,    61,    62,    21,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,     0,     0,    74,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,    38,    39,     0,     0,
       0,     0,     4,    41,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,     0,    12,    13,     0,     0,
       0,    14,     0,   312,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,    61,    62,    21,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,     0,     0,    74,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,     0,     0,     0,     0,    35,    36,
      37,    38,    39,     0,     0,     0,     0,     4,    41,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,   351,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,    61,    62,    21,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,     0,     0,    74,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,    38,    39,     0,     0,
       0,     0,     4,    41,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,     0,    12,    13,     0,     0,
       0,    14,     0,   464,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,    61,    62,    21,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,     0,     0,    74,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,     0,     0,     0,     0,    35,    36,
      37,    38,    39,     0,     0,     0,     0,     4,    41,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,   503,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,    61,    62,    21,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,     0,     0,    74,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,    38,    39,     0,     0,
       0,     0,     4,    41,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,     0,    12,    13,     0,     0,
       0,    14,     0,   505,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,    61,    62,    21,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,     0,     0,    74,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,     0,     0,     0,     0,    35,    36,
      37,    38,    39,     0,     0,     0,     0,     4,    41,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,   610,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,    61,    62,    21,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,     0,     0,    74,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,    38,    39,     0,     0,
       0,     0,     4,    41,     6,     0,     0,     7,     8,     0,
       9,    10,     0,     0,     0,     0,    12,    13,     0,     0,
       0,    14,     0,     0,     0,     0,     0,    15,     0,     0,
       0,    16,    17,    18,    19,    20,     0,    61,    62,    21,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,     0,     0,     0,     0,     0,     0,    74,
       0,     0,    22,    23,    24,    25,    26,    27,    28,    29,
      30,    31,    32,    33,     0,     0,     0,     0,    35,    36,
      37,    38,    39,     0,     0,     0,     0,     4,    41,     6,
       0,     0,     7,     8,     0,     9,    10,     0,     0,     0,
       0,    12,    13,     0,     0,     0,    14,     0,     0,     0,
       0,     0,    15,     0,     0,     0,    16,    17,    18,    19,
      20,     0,    61,    62,    21,     0,     0,     0,     0,     0,
       0,    66,    67,    68,    69,    70,    71,    72,     0,     0,
       0,     0,     0,     0,    74,     0,     0,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,     0,
       0,     0,     0,    35,    36,    37,   376,    39,     0,     0,
       0,     0,     0,    41,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    61,    62,     0,
       0,     0,     0,     0,     0,     0,    66,    67,    68,    69,
      70,    71,    72,   185,     0,     0,   186,   187,     0,    74,
     188,   189,     0,     0,     0,   190,   191,     0,   192,     0,
     193,   194,   195,     0,     0,     0,     0,   196,     0,     0,
       0,     0,     0,     0,   197,   198,   199,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   200,
     201,   202,   203,   204,   205,   206,   207,   208,   209,   210,
     185,     0,     0,   186,   187,     0,     0,   188,   189,     0,
       0,     0,   190,   191,     0,   192,     0,   193,   194,   195,
       0,     0,     0,     0,   196,     0,     0,     0,     0,     0,
       0,   197,   198,   199,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,   200,   201,   202,   203,
     204,   205,   206,   207,   208,   209,   210,   637,     0,   185,
     638,     0,   186,   187,     0,     0,   188,   189,   426,     0,
       0,   190,   191,     0,   192,     0,   193,   194,   195,     0,
       0,     0,     0,   196,     0,     0,     0,     0,     0,     0,
     197,   198,   199,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   200,   201,   202,   203,   204,
     205,   206,   207,   208,   209,   210,   185,     0,     0,   186,
     187,     0,     0,   188,   189,   555,     0,     0,   190,   191,
       0,   192,     0,   193,   194,   195,     0,     0,     0,     0,
     196,   232,     0,     0,     0,     0,     0,   197,   198,   199,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   200,   201,   202,   203,   204,   205,   206,   207,
     208,   209,   210,   185,     0,     0,   186,   187,     0,     0,
     188,   189,     0,     0,     0,   190,   191,     0,   192,     0,
     193,   194,   195,     0,   380,     0,     0,   196,     0,     0,
       0,     0,     0,     0,   197,   198,   199,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   200,
     201,   202,   203,   204,   205,   206,   207,   208,   209,   210,
     185,     0,     0,   186,   187,     0,     0,   188,   189,     0,
       0,     0,   190,   191,     0,   192,     0,   193,   194,   195,
       0,     0,     0,     0,   196,   381,     0,     0,     0,     0,
       0,   197,   198,   199,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,   200,   201,   202,   203,
     204,   205,   206,   207,   208,   209,   210,   185,     0,     0,
     186,   187,     0,     0,   188,   189,     0,     0,     0,   190,
     191,     0,   192,     0,   193,   194,   195,     0,     0,     0,
       0,   196,   386,     0,     0,     0,     0,     0,   197,   198,
     199,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   200,   201,   202,   203,   204,   205,   206,
     207,   208,   209,   210,   185,     0,     0,   186,   187,     0,
       0,   188,   189,     0,     0,     0,   190,   191,     0,   192,
       0,   193,   194,   195,     0,     0,     0,     0,   196,   387,
       0,     0,     0,     0,     0,   197,   198,   199,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     200,   201,   202,   203,   204,   205,   206,   207,   208,   209,
     210,   185,     0,     0,   186,   187,     0,     0,   188,   189,
       0,     0,     0,   190,   191,     0,   192,     0,   193,   194,
     195,     0,     0,     0,     0,   196,   394,     0,     0,     0,
       0,     0,   197,   198,   199,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   200,   201,   202,
     203,   204,   205,   206,   207,   208,   209,   210,   185,     0,
       0,   186,   187,     0,     0,   188,   189,     0,     0,     0,
     190,   191,     0,   192,     0,   193,   194,   195,     0,     0,
       0,     0,   196,   402,     0,     0,     0,     0,     0,   197,
     198,   199,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   200,   201,   202,   203,   204,   205,
     206,   207,   208,   209,   210,   185,   440,     0,   186,   187,
       0,     0,   188,   189,     0,     0,     0,   190,   191,     0,
     192,     0,   193,   194,   195,     0,     0,     0,     0,   196,
       0,     0,     0,     0,     0,     0,   197,   198,   199,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   200,   201,   202,   203,   204,   205,   206,   207,   208,
     209,   210,   185,     0,     0,   186,   187,     0,     0,   188,
     189,     0,     0,     0,   190,   191,     0,   192,     0,   193,
     194,   195,     0,   449,     0,     0,   196,     0,     0,     0,
       0,     0,     0,   197,   198,   199,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,   200,   201,
     202,   203,   204,   205,   206,   207,   208,   209,   210,   185,
       0,     0,   186,   187,     0,     0,   188,   189,     0,     0,
       0,   190,   191,     0,   192,     0,   193,   194,   195,     0,
     457,     0,     0,   196,     0,     0,     0,     0,     0,     0,
     197,   198,   199,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   200,   201,   202,   203,   204,
     205,   206,   207,   208,   209,   210,   185,     0,     0,   186,
     187,     0,     0,   188,   189,     0,     0,     0,   190,   191,
       0,   192,     0,   193,   194,   195,     0,   491,     0,     0,
     196,     0,     0,     0,     0,     0,     0,   197,   198,   199,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   200,   201,   202,   203,   204,   205,   206,   207,
     208,   209,   210,   185,     0,     0,   186,   187,     0,     0,
     188,   189,     0,     0,     0,   190,   191,     0,   192,     0,
     193,   194,   195,     0,   515,     0,     0,   196,     0,     0,
       0,     0,     0,     0,   197,   198,   199,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   200,
     201,   202,   203,   204,   205,   206,   207,   208,   209,   210,
     185,     0,     0,   186,   187,     0,     0,   188,   189,     0,
       0,     0,   190,   191,     0,   192,     0,   193,   194,   195,
       0,     0,     0,     0,   196,   528,     0,     0,     0,     0,
       0,   197,   198,   199,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,   200,   201,   202,   203,
     204,   205,   206,   207,   208,   209,   210,   185,     0,     0,
     186,   187,     0,     0,   188,   189,     0,     0,     0,   190,
     191,     0,   192,     0,   193,   194,   195,     0,   579,     0,
       0,   196,     0,     0,     0,     0,     0,     0,   197,   198,
     199,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   200,   201,   202,   203,   204,   205,   206,
     207,   208,   209,   210,   185,     0,     0,   186,   187,     0,
       0,   188,   189,     0,     0,     0,   190,   191,     0,   192,
       0,   193,   194,   195,     0,     0,     0,   581,   196,     0,
       0,     0,     0,     0,     0,   197,   198,   199,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     200,   201,   202,   203,   204,   205,   206,   207,   208,   209,
     210,   185,     0,     0,   186,   187,     0,     0,   188,   189,
       0,     0,     0,   190,   191,     0,   192,     0,   193,   194,
     195,     0,     0,     0,     0,   196,   682,     0,     0,     0,
       0,     0,   197,   198,   199,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   200,   201,   202,
     203,   204,   205,   206,   207,   208,   209,   210,   185,     0,
       0,   186,   187,     0,     0,   188,   189,     0,     0,     0,
     190,   191,     0,   192,     0,   193,   194,   195,     0,     0,
       0,     0,   196,   693,     0,     0,     0,     0,     0,   197,
     198,   199,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   200,   201,   202,   203,   204,   205,
     206,   207,   208,   209,   210,   185,     0,     0,   186,   187,
       0,     0,   188,   189,     0,     0,     0,   190,   191,     0,
     192,     0,   193,   194,   195,     0,     0,     0,     0,   196,
       0,     0,     0,     0,     0,     0,   197,   198,   199,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   200,   201,   202,   203,   204,   205,   206,   207,   208,
     209,   210,   185,     0,     0,   186,   187,     0,     0,   188,
     189,     0,     0,     0,   190,   191,     0,   192,     0,   193,
     194,   195,     0,     0,     0,     0,   196,     0,     0,     0,
       0,     0,     0,     0,   198,   199,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,   200,   201,
     202,   203,   204,   205,   206,   207,   208,   209,   210,   185,
       0,     0,   186,   187,     0,     0,   188,   189,     0,     0,
       0,   190,   191,     0,   192,     0,   193,   194,   195,     0,
       0,     0,     0,   196,     0,     0,     0,     0,     0,     0,
       0,     0,   199,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   200,   201,   202,   203,   204,
     205,   206,   207,   208,   209,   210,   185,     0,     0,   186,
     187,     0,     0,   188,   189,     0,     0,     0,   190,   191,
       0,   192,     0,   193,   194,   195,     0,     0,     0,     0,
     196,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   200,   201,   202,   203,   204,   205,   206,   207,
     208,   209,   210,   185,     0,     0,   186,   187,     0,     0,
     188,   189,     0,     0,     0,   190,   191,     0,     0,     0,
     193,   194,   195,     0,     0,     0,     0,   196,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   200,
     201,   202,   203,   204,   205,   206,   207,   208,   209,   210,
     185,     0,     0,   186,   187,     0,     0,   188,   189,     0,
       0,     0,   190,   191,     0,     0,     0,   193,   194,   195,
       0,     0,   185,     0,   196,   186,   187,     0,     0,   188,
     189,     0,     0,     0,   190,   191,     0,     0,     0,   193,
     194,   195,     0,     0,     0,     0,   196,   201,   202,   203,
     204,   205,   206,   207,   208,   209,   210,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     202,   203,   204,   205,   206,   207,   208,   209,   210,   185,
       0,     0,   186,   187,     0,     0,   188,   189,     0,     0,
       0,   190,   191,     0,     0,     0,   193,   194,   195,   185,
       0,     0,   186,   187,     0,     0,     0,   189,     0,     0,
       0,   190,   191,     0,     0,     0,   193,   194,   195,     0,
       0,     0,     0,     0,     0,     0,     0,   202,   203,   204,
     205,   206,   207,   208,   209,   210,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   202,   203,   204,
     205,   206,   207,   208,   209,   210,   185,     0,     0,   186,
     187,     0,     0,     0,   189,     0,     0,     0,   190,   191,
       0,     0,     0,   193,   185,   195,     0,   186,   187,     0,
       0,     0,   189,     0,     0,     0,   190,   191,     0,     0,
       0,   193,     0,   195,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   202,   203,   204,   205,   206,   207,
     208,   209,   210,     0,     0,     0,     0,     0,     0,     0,
       0,     0,  -293,  -293,  -293,  -293,   206,   207,   208,   209,
     210
};

#define yypact_value_is_default(yystate) \
  ((yystate) == (-389))

#define yytable_value_is_error(yytable_value) \
  ((yytable_value) == (-293))

static const yytype_int16 yycheck[] =
{
      29,    30,   162,    32,    32,     2,    49,    50,   226,    52,
     262,    32,   184,    32,   312,   210,    52,     5,     4,    17,
       4,     4,    27,     4,     4,     4,    27,    31,     4,    24,
       5,     9,    10,   395,    20,    16,    20,    20,    29,    30,
      20,    20,   260,    16,    16,     5,    43,    90,    10,    26,
      16,    17,    14,    17,    26,   227,    17,     8,   399,    17,
       5,    52,     5,    25,    16,    16,     0,   408,   409,    24,
       5,    16,   100,     5,    26,    26,    27,    81,    29,    17,
      20,    26,    24,    81,    16,    83,   258,   259,    83,    81,
     262,    31,    29,    30,    26,   483,    74,    20,   103,   104,
      62,   144,   103,   104,    16,    17,    17,   143,    31,    17,
     153,   139,   155,   149,    89,    81,   159,    83,   161,    83,
     163,    27,    83,   166,     4,    83,   169,     7,    83,   133,
      81,   174,    83,    84,   132,    86,    87,    88,   167,   126,
      17,   133,     5,    81,     5,    83,   175,   176,   177,   537,
     179,   539,   143,   130,   495,   496,   184,   143,   149,   143,
     143,   149,   143,   143,   143,   184,   464,   143,   530,    81,
      81,    83,    83,    81,     5,    83,   167,    70,    71,   130,
      16,   210,   210,     5,   175,   176,   177,    20,   179,   210,
     226,   210,    17,    17,   535,     5,   147,   148,    31,   227,
      24,   126,   108,     5,    81,   503,    83,   505,   227,   115,
     116,   117,   118,   119,   120,   121,    83,   246,     5,   437,
     438,    20,    76,    77,   260,     6,     5,   445,     9,    10,
     258,   259,    31,    14,   262,   226,    20,    18,   267,   258,
     259,   107,     5,   262,    25,     5,   441,    31,    73,   115,
     116,   117,   118,   119,   120,   246,    81,    81,    83,    83,
       5,    20,    81,   604,   307,   606,     7,   608,    20,   260,
     149,   523,    31,    27,    81,    16,   267,     5,   130,    31,
       5,    62,    62,   312,    81,   149,    83,    84,   317,   149,
     319,   102,   103,   104,    76,    77,   324,   102,   103,   104,
      11,   149,   462,   644,     5,    16,    92,   130,   468,   650,
     651,    81,   610,    81,    89,    26,    27,    15,    29,   479,
     128,   312,   351,    31,   128,   577,    31,     6,     5,   670,
       9,    10,   673,    20,     5,    14,     4,   678,    20,    18,
      19,   298,   100,    15,    23,   686,    25,     5,    31,   690,
      83,   523,     5,    31,   108,   391,    31,    20,    81,    16,
     351,   115,   116,   117,   118,   119,   120,   121,   397,   398,
      81,     5,    83,    84,   417,    86,    87,    88,   129,    58,
      59,    60,    61,    62,     5,    31,     5,   416,    29,   418,
     387,    81,    27,   422,   423,    26,    81,   394,    31,    31,
     391,   437,   438,   432,   401,   577,   397,   398,   111,   445,
     149,     5,   441,   441,    15,    15,   459,    16,    20,   130,
     441,    29,   441,   130,     4,   416,    15,   418,     4,   131,
       4,   422,   423,    31,    20,   464,   147,   148,     5,    81,
      31,   601,   399,    31,    31,   474,   437,   438,    29,    12,
     130,   408,   409,    16,   445,    16,    31,    83,    31,    15,
      31,   131,    20,    26,    27,    81,    29,   107,    83,   629,
      27,     5,    31,   464,   503,    15,   505,    83,    15,   639,
     509,     5,    15,   474,    81,   528,     7,    83,    78,    15,
      31,    83,   131,    15,     5,   523,    81,    15,   658,     9,
      16,   661,   531,    15,   523,     7,   666,   111,    18,     5,
      16,    31,   503,    83,   505,   708,   545,    31,    81,   416,
      83,    84,     2,    86,    87,    88,     2,   616,   688,   572,
     432,   475,   531,   592,   636,   691,   542,   508,   495,   496,
     531,   701,   549,   296,   570,   588,   514,   167,   620,   577,
     710,   594,   448,   596,   422,    -1,    -1,    -1,   577,    -1,
     720,   423,    -1,     4,    -1,    -1,    -1,   130,    -1,    79,
      80,    81,   615,    -1,    15,    -1,    -1,    -1,   535,    89,
      -1,   610,    -1,    -1,   147,   148,    -1,    -1,   585,    -1,
      -1,   634,    -1,    -1,   591,   592,    -1,    -1,    -1,    -1,
      41,    42,    43,    44,    45,    46,    47,    48,    49,    50,
      51,    -1,    -1,    -1,    -1,    -1,   659,    -1,    -1,   610,
      -1,    -1,    -1,   133,   134,   135,   136,   137,   138,    70,
      71,   628,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   684,    -1,    -1,    -1,    -1,    -1,   604,    -1,   606,
      -1,   608,   695,     4,    -1,     6,    -1,     8,    -1,    -1,
      -1,   704,    13,    14,    15,    16,    17,    -1,    19,    20,
      21,    22,    23,    24,    25,    26,    27,    28,    -1,    16,
      31,    -1,    -1,    -1,   713,   682,    -1,   644,    -1,    26,
      27,    42,    29,   650,   651,    -1,    -1,    -1,    49,    50,
      -1,    52,   143,    54,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,   670,    -1,    -1,   673,    -1,    -1,    -1,
      -1,   678,     4,    -1,     6,    -1,    -1,     9,    10,   686,
      -1,    13,    14,   690,    -1,    -1,    18,    19,    -1,    21,
      -1,    23,    24,    25,    81,    -1,    83,    84,    30,    86,
      87,    88,    -1,    -1,    -1,    37,    38,    39,    -1,   110,
      -1,    -1,    -1,    -1,    -1,   116,    -1,    -1,    -1,    -1,
      52,    53,    54,    55,    56,    57,    58,    59,    60,    61,
      62,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   140,
      -1,   142,   143,   130,    -1,    -1,   147,   148,   149,    -1,
     151,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   146,
     147,   148,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   180,
      -1,    -1,    -1,    -1,   185,   186,   187,   188,   189,   190,
     191,   192,   193,   194,   195,   196,   197,   198,   199,   200,
     201,   202,   203,   204,   205,   206,   207,   208,   209,    -1,
      -1,   143,    -1,   214,   215,   216,   217,   218,   219,   220,
     221,   222,   223,   224,   225,   226,    -1,   228,   229,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,     3,     4,     5,
      -1,    -1,     8,     9,   245,    11,    12,    -1,    -1,    -1,
      16,    17,    18,    -1,    -1,    15,    22,    -1,    -1,   260,
      -1,    27,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,   272,    -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,
      -1,    41,    42,    43,    44,    45,    46,    47,    48,    49,
      50,    51,   293,    -1,    -1,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    75,
      70,    71,    -1,    79,    80,    81,    82,    83,    -1,    85,
      -1,    -1,    -1,    89,    90,    91,    92,    -1,    94,    -1,
      96,    -1,    98,    -1,    -1,   101,    -1,    -1,    -1,   105,
     106,   107,    -1,   109,   110,    -1,   112,   113,   114,    -1,
      -1,    -1,   118,   119,   120,    -1,   122,   123,   124,   125,
     126,   127,    -1,    -1,   365,    -1,   132,   133,   134,   135,
     136,   137,   138,    15,    -1,    -1,    -1,   143,    -1,   145,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     391,    -1,   393,    -1,   395,   396,    -1,    -1,    -1,    41,
      42,    43,    44,    45,    46,    47,    48,    49,    50,    51,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,   426,    -1,   428,    70,    71,
      -1,    -1,    -1,    15,    -1,    -1,   437,   438,    20,   440,
      -1,    -1,    -1,    -1,   445,    -1,   447,   448,    -1,    31,
      -1,    -1,    -1,    -1,    -1,   456,    -1,    -1,   100,    41,
      42,    43,    44,    45,    46,    47,    48,    49,    50,    51,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    70,    71,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   502,    -1,    -1,    -1,    -1,     3,     4,     5,    -1,
      -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,    16,
      17,    18,    -1,    -1,    -1,    22,    -1,    -1,    -1,   530,
      27,    28,    -1,    -1,    -1,    32,    33,    34,    35,    36,
      -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,   555,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    63,    64,    65,    66,
      67,    68,    69,    70,    71,    72,    73,    74,    75,    -1,
      -1,    -1,    79,    80,    81,    82,    83,    -1,    85,    -1,
      -1,    -1,    89,    90,    91,    92,   597,    94,    -1,    96,
      -1,    98,    -1,    -1,   101,    -1,    -1,    -1,   105,   106,
     107,    -1,   109,   110,    -1,   112,   113,   114,    -1,    -1,
      -1,   118,   119,   120,   625,   122,   123,   124,   125,   126,
     127,    -1,    -1,    -1,    -1,   132,   133,   134,   135,   136,
     137,   138,    -1,    -1,    -1,    -1,   143,    -1,   145,     3,
       4,     5,    -1,    -1,     8,     9,   657,    11,    12,    -1,
      -1,    -1,    16,    17,    18,    -1,    -1,    15,    22,    -1,
      -1,    -1,    20,    -1,    28,    -1,    -1,    -1,    32,    33,
      34,    35,    36,    31,    -1,    -1,    40,    -1,    -1,    -1,
      -1,    -1,    -1,    41,    42,    43,    44,    45,    46,    47,
      48,    49,    50,    51,    -1,    -1,    -1,    -1,    -1,    63,
      64,    65,    66,    67,    68,    69,    70,    71,    72,    73,
      74,    75,    70,    71,    -1,    79,    80,    81,    82,    83,
      -1,    85,    -1,    -1,    -1,    89,    90,    91,    92,    93,
      94,    -1,    96,    -1,    98,    -1,    -1,   101,    -1,    -1,
      -1,   105,   106,   107,    -1,   109,   110,    -1,   112,   113,
     114,    -1,    -1,    -1,   118,   119,   120,    -1,   122,   123,
     124,   125,   126,   127,    -1,    -1,    -1,    -1,   132,   133,
     134,   135,   136,   137,   138,    -1,    -1,    -1,    -1,   143,
      -1,   145,     3,     4,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    16,    17,    18,    -1,    -1,
      -1,    22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,    -1,    -1,    40,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    75,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    85,    -1,    -1,    -1,    89,    90,
      91,    92,    -1,    94,    -1,    96,    -1,    98,    99,    -1,
     101,    -1,    -1,    -1,   105,   106,   107,    -1,   109,   110,
      -1,   112,   113,   114,    -1,    -1,    -1,   118,   119,   120,
      -1,   122,   123,   124,   125,   126,   127,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,   143,    -1,   145,     3,     4,     5,    -1,    -1,
       8,     9,    -1,    11,    12,    -1,    -1,    -1,    16,    17,
      18,    -1,    -1,    -1,    22,    -1,    -1,    -1,    -1,    27,
      28,    -1,    -1,    -1,    32,    33,    34,    35,    36,    -1,
      -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    63,    64,    65,    66,    67,
      68,    69,    70,    71,    72,    73,    74,    75,    -1,    -1,
      -1,    79,    80,    81,    82,    83,    -1,    85,    -1,    -1,
      -1,    89,    90,    91,    92,    -1,    94,    -1,    96,    -1,
      98,    -1,    -1,   101,    -1,    -1,    -1,   105,   106,   107,
      -1,   109,   110,    -1,   112,   113,   114,    -1,    -1,    -1,
     118,   119,   120,    -1,   122,   123,   124,   125,   126,   127,
      -1,    -1,    -1,    -1,   132,   133,   134,   135,   136,   137,
     138,    -1,    -1,    -1,    -1,   143,    -1,   145,     3,     4,
       5,    -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,
      -1,    16,    17,    18,    -1,    -1,    -1,    22,    -1,    -1,
      -1,    -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,
      35,    36,    -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    63,    64,
      65,    66,    67,    68,    69,    70,    71,    72,    73,    74,
      75,    -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,
      85,    -1,    -1,    -1,    89,    90,    91,    92,    -1,    94,
      -1,    96,    97,    98,    -1,    -1,   101,    -1,    -1,    -1,
     105,   106,   107,    -1,   109,   110,    -1,   112,   113,   114,
      -1,    -1,    -1,   118,   119,   120,    -1,   122,   123,   124,
     125,   126,   127,    -1,    -1,    -1,    -1,   132,   133,   134,
     135,   136,   137,   138,    -1,    -1,    -1,    -1,   143,    -1,
     145,     3,     4,     5,    -1,    -1,     8,     9,    -1,    11,
      12,    -1,    -1,    -1,    16,    17,    18,    -1,    -1,    -1,
      22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,    -1,
      32,    33,    34,    35,    36,    -1,    -1,    -1,    40,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    63,    64,    65,    66,    67,    68,    69,    70,    71,
      72,    73,    74,    75,    -1,    -1,    -1,    79,    80,    81,
      82,    83,    -1,    85,    -1,    -1,    -1,    89,    90,    91,
      92,    -1,    94,    95,    96,    -1,    98,    -1,    -1,   101,
      -1,    -1,    -1,   105,   106,   107,    -1,   109,   110,    -1,
     112,   113,   114,    -1,    -1,    -1,   118,   119,   120,    -1,
     122,   123,   124,   125,   126,   127,    -1,    -1,    -1,    -1,
     132,   133,   134,   135,   136,   137,   138,    -1,    -1,    -1,
      -1,   143,    -1,   145,     3,     4,     5,    -1,    -1,     8,
       9,    -1,    11,    12,    -1,    -1,    -1,    16,    17,    18,
      -1,    -1,    -1,    22,    -1,    -1,    -1,    -1,    27,    28,
      -1,    -1,    -1,    32,    33,    34,    35,    36,    -1,    -1,
      -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    63,    64,    65,    66,    67,    68,
      69,    70,    71,    72,    73,    74,    75,    -1,    -1,    -1,
      79,    80,    81,    82,    83,    -1,    85,    -1,    -1,    -1,
      89,    90,    91,    92,    -1,    94,    -1,    96,    -1,    98,
      -1,    -1,   101,    -1,    -1,    -1,   105,   106,   107,    -1,
     109,   110,    -1,   112,   113,   114,    -1,    -1,    -1,   118,
     119,   120,    -1,   122,   123,   124,   125,   126,   127,    -1,
      -1,    -1,    -1,   132,   133,   134,   135,   136,   137,   138,
      -1,    -1,    -1,    -1,   143,    -1,   145,     3,     4,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      16,    17,    18,    -1,    -1,    -1,    22,    -1,    -1,    -1,
      -1,    27,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    75,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    85,
      -1,    -1,    -1,    89,    90,    91,    92,    -1,    94,    -1,
      96,    -1,    98,    -1,    -1,   101,    -1,    -1,    -1,   105,
     106,   107,    -1,   109,   110,    -1,   112,   113,   114,    -1,
      -1,    -1,   118,   119,   120,    -1,   122,   123,   124,   125,
     126,   127,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,   143,    -1,   145,
       3,     4,     5,    -1,    -1,     8,     9,    -1,    11,    12,
      -1,    -1,    -1,    16,    17,    18,    -1,    -1,    -1,    22,
      -1,    -1,    -1,    -1,    27,    28,    -1,    -1,    -1,    32,
      33,    34,    35,    36,    -1,    -1,    -1,    40,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      63,    64,    65,    66,    67,    68,    69,    70,    71,    72,
      73,    74,    75,    -1,    -1,    -1,    79,    80,    81,    82,
      83,    -1,    85,    -1,    -1,    -1,    89,    90,    91,    92,
      -1,    94,    -1,    96,    -1,    98,    -1,    -1,   101,    -1,
      -1,    -1,   105,   106,   107,    -1,   109,   110,    -1,   112,
     113,   114,    -1,    -1,    -1,   118,   119,   120,    -1,   122,
     123,   124,   125,   126,   127,    -1,    -1,    -1,    -1,   132,
     133,   134,   135,   136,   137,   138,    -1,    -1,    -1,    -1,
     143,    -1,   145,     3,     4,     5,    -1,    -1,     8,     9,
      -1,    11,    12,    -1,    -1,    -1,    16,    17,    18,    -1,
      -1,    -1,    22,    -1,    -1,    -1,    -1,    -1,    28,    -1,
      -1,    -1,    32,    33,    34,    35,    36,    -1,    -1,    -1,
      40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    63,    64,    65,    66,    67,    68,    69,
      70,    71,    72,    73,    74,    75,    -1,    -1,    -1,    79,
      80,    81,    82,    83,    -1,    85,    -1,    -1,    -1,    89,
      90,    91,    92,    -1,    94,    -1,    96,    -1,    98,    -1,
      -1,   101,    -1,    -1,    -1,   105,   106,   107,    -1,   109,
     110,    -1,   112,   113,   114,    -1,    -1,    -1,   118,   119,
     120,    -1,   122,   123,   124,   125,   126,   127,    -1,    -1,
      -1,    -1,   132,   133,   134,   135,   136,   137,   138,    -1,
      -1,    -1,    -1,   143,    -1,   145,     3,     4,     5,    -1,
      -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,    16,
      17,    18,    -1,    -1,    -1,    22,    -1,    -1,    -1,    -1,
      -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,    36,
      -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    63,    64,    65,    66,
      67,    68,    69,    70,    71,    72,    73,    74,    75,    -1,
      -1,    -1,    79,    80,    81,    82,    83,    -1,    85,    -1,
      -1,    -1,    89,    90,    91,    92,    -1,    94,    -1,    96,
      -1,    98,    -1,    -1,   101,    -1,    -1,    -1,   105,   106,
     107,    -1,   109,   110,    -1,   112,   113,   114,    -1,    -1,
      -1,   118,   119,   120,    -1,   122,   123,   124,   125,   126,
     127,    -1,    -1,    -1,    -1,   132,   133,   134,   135,   136,
     137,   138,    -1,    -1,    -1,    -1,   143,    -1,   145,     3,
       4,     5,    -1,     7,     8,     9,    -1,    11,    12,    -1,
      -1,    -1,    16,    17,    18,    -1,    -1,    -1,    22,    -1,
      -1,    -1,    -1,    -1,    28,    -1,    -1,    -1,    32,    33,
      34,    35,    36,    -1,    -1,    -1,    40,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    63,
      64,    65,    66,    67,    68,    69,    70,    71,    72,    73,
      74,    75,    -1,    -1,    -1,    79,    80,    81,    82,    83,
      -1,    85,    -1,    -1,    -1,    89,    90,    91,    92,    -1,
      94,    -1,    96,    -1,    98,    -1,    -1,   101,    -1,    -1,
      -1,   105,   106,    -1,    -1,   109,   110,    -1,   112,   113,
     114,    -1,    -1,    -1,    -1,    -1,   120,    -1,   122,   123,
     124,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   132,   133,
     134,   135,   136,   137,   138,    -1,    -1,    -1,    -1,   143,
      -1,   145,     3,     4,     5,    -1,     7,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    16,    17,    18,    -1,    -1,
      -1,    22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,    -1,    -1,    40,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    75,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    85,    -1,    -1,    -1,    89,    90,
      91,    92,    -1,    94,    -1,    96,    -1,    98,    -1,    -1,
     101,    -1,    -1,    -1,   105,   106,    -1,    -1,   109,   110,
      -1,   112,   113,   114,    -1,    -1,    -1,    -1,    -1,   120,
      -1,   122,   123,   124,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,   143,    -1,   145,     3,     4,     5,    -1,     7,
       8,     9,    -1,    11,    12,    -1,    -1,    -1,    16,    17,
      18,    -1,    -1,    -1,    22,    -1,    -1,    -1,    -1,    -1,
      28,    -1,    -1,    -1,    32,    33,    34,    35,    36,    -1,
      -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    63,    64,    65,    66,    67,
      68,    69,    70,    71,    72,    73,    74,    75,    -1,    -1,
      -1,    79,    80,    81,    82,    83,    -1,    85,    -1,    -1,
      -1,    89,    90,    91,    92,    -1,    94,    -1,    96,    -1,
      98,    -1,    -1,   101,    -1,    -1,    -1,   105,   106,    -1,
      -1,   109,   110,    -1,   112,   113,   114,    -1,    -1,    -1,
      -1,    -1,   120,    -1,   122,   123,   124,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,   132,   133,   134,   135,   136,   137,
     138,    -1,    -1,    -1,    -1,   143,    -1,   145,     3,     4,
       5,    -1,     7,     8,     9,    -1,    11,    12,    -1,    -1,
      -1,    16,    17,    18,    -1,    -1,    -1,    22,    -1,    -1,
      -1,    -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,
      35,    36,    -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    63,    64,
      65,    66,    67,    68,    69,    70,    71,    72,    73,    74,
      75,    -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,
      85,    -1,    -1,    -1,    89,    90,    91,    92,    -1,    94,
      -1,    96,    -1,    98,    -1,    -1,   101,    -1,    -1,    -1,
     105,   106,    -1,    -1,   109,   110,    -1,   112,   113,   114,
      -1,    -1,    -1,    -1,    -1,   120,    -1,   122,   123,   124,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,
     135,   136,   137,   138,    -1,    -1,    -1,    -1,   143,    -1,
     145,     3,     4,     5,    -1,     7,     8,     9,    -1,    11,
      12,    -1,    -1,    -1,    16,    17,    18,    -1,    -1,    -1,
      22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,    -1,
      32,    33,    34,    35,    36,    -1,    -1,    -1,    40,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    63,    64,    65,    66,    67,    68,    69,    70,    71,
      72,    73,    74,    75,    -1,    -1,    -1,    79,    80,    81,
      82,    83,    -1,    85,    -1,    -1,    -1,    89,    90,    91,
      92,    -1,    94,    -1,    96,    -1,    98,    -1,    -1,   101,
      -1,    -1,    -1,   105,   106,    -1,    -1,   109,   110,    -1,
     112,   113,   114,    -1,    -1,    -1,    -1,    -1,   120,    -1,
     122,   123,   124,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     132,   133,   134,   135,   136,   137,   138,    -1,    -1,    -1,
      -1,   143,    -1,   145,     3,     4,     5,    -1,    -1,     8,
       9,    -1,    11,    12,    -1,    -1,    -1,    16,    17,    18,
      -1,    -1,    -1,    22,    -1,    -1,    -1,    -1,    -1,    28,
      -1,    -1,    -1,    32,    33,    34,    35,    36,    -1,    -1,
      -1,    40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    63,    64,    65,    66,    67,    68,
      69,    70,    71,    72,    73,    74,    75,    -1,    -1,    -1,
      79,    80,    81,    82,    83,    -1,    85,    -1,    -1,    -1,
      89,    90,    91,    92,    -1,    94,    -1,    96,    -1,    98,
      -1,    -1,   101,    -1,    -1,    -1,   105,   106,    -1,    -1,
     109,   110,    -1,   112,   113,   114,    -1,    -1,    -1,    -1,
      -1,   120,    -1,   122,   123,   124,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,   132,   133,   134,   135,   136,   137,   138,
      -1,    -1,    -1,    -1,   143,    -1,   145,     3,     4,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    -1,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,    -1,    -1,    40,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,     3,    89,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    -1,    17,    18,    -1,    -1,
      -1,    22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,
      31,    32,    33,    34,    35,    36,    -1,   123,   124,    40,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,   143,    -1,   145,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    -1,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    -1,    -1,    -1,     3,    89,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    24,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,   123,   124,    40,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,    -1,    -1,   145,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,     3,    89,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    -1,    17,    18,    -1,    -1,
      -1,    22,    -1,    24,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,   123,   124,    40,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,    -1,    -1,   145,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    -1,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    -1,    -1,    -1,     3,    89,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    24,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,   123,   124,    40,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,    -1,    -1,   145,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,     3,    89,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    -1,    17,    18,    -1,    -1,
      -1,    22,    -1,    24,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,   123,   124,    40,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,    -1,    -1,   145,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    -1,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    -1,    -1,    -1,     3,    89,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    24,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,   123,   124,    40,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,    -1,    -1,   145,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,     3,    89,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    -1,    17,    18,    -1,    -1,
      -1,    22,    -1,    24,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,   123,   124,    40,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,    -1,    -1,   145,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    -1,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    -1,    -1,    -1,     3,    89,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    24,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,   123,   124,    40,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,    -1,    -1,   145,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,     3,    89,     5,    -1,    -1,     8,     9,    -1,
      11,    12,    -1,    -1,    -1,    -1,    17,    18,    -1,    -1,
      -1,    22,    -1,    -1,    -1,    -1,    -1,    28,    -1,    -1,
      -1,    32,    33,    34,    35,    36,    -1,   123,   124,    40,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,    -1,    -1,    -1,    -1,    -1,    -1,   145,
      -1,    -1,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    -1,    -1,    -1,    -1,    79,    80,
      81,    82,    83,    -1,    -1,    -1,    -1,     3,    89,     5,
      -1,    -1,     8,     9,    -1,    11,    12,    -1,    -1,    -1,
      -1,    17,    18,    -1,    -1,    -1,    22,    -1,    -1,    -1,
      -1,    -1,    28,    -1,    -1,    -1,    32,    33,    34,    35,
      36,    -1,   123,   124,    40,    -1,    -1,    -1,    -1,    -1,
      -1,   132,   133,   134,   135,   136,   137,   138,    -1,    -1,
      -1,    -1,    -1,    -1,   145,    -1,    -1,    63,    64,    65,
      66,    67,    68,    69,    70,    71,    72,    73,    74,    -1,
      -1,    -1,    -1,    79,    80,    81,    82,    83,    -1,    -1,
      -1,    -1,    -1,    89,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,   123,   124,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,   132,   133,   134,   135,
     136,   137,   138,     6,    -1,    -1,     9,    10,    -1,   145,
      13,    14,    -1,    -1,    -1,    18,    19,    -1,    21,    -1,
      23,    24,    25,    -1,    -1,    -1,    -1,    30,    -1,    -1,
      -1,    -1,    -1,    -1,    37,    38,    39,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,
      53,    54,    55,    56,    57,    58,    59,    60,    61,    62,
       6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,
      -1,    -1,    18,    19,    -1,    21,    -1,    23,    24,    25,
      -1,    -1,    -1,    -1,    30,    -1,    -1,    -1,    -1,    -1,
      -1,    37,    38,    39,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,
      56,    57,    58,    59,    60,    61,    62,     4,    -1,     6,
       7,    -1,     9,    10,    -1,    -1,    13,    14,   131,    -1,
      -1,    18,    19,    -1,    21,    -1,    23,    24,    25,    -1,
      -1,    -1,    -1,    30,    -1,    -1,    -1,    -1,    -1,    -1,
      37,    38,    39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,    56,
      57,    58,    59,    60,    61,    62,     6,    -1,    -1,     9,
      10,    -1,    -1,    13,    14,   131,    -1,    -1,    18,    19,
      -1,    21,    -1,    23,    24,    25,    -1,    -1,    -1,    -1,
      30,    31,    -1,    -1,    -1,    -1,    -1,    37,    38,    39,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    52,    53,    54,    55,    56,    57,    58,    59,
      60,    61,    62,     6,    -1,    -1,     9,    10,    -1,    -1,
      13,    14,    -1,    -1,    -1,    18,    19,    -1,    21,    -1,
      23,    24,    25,    -1,    27,    -1,    -1,    30,    -1,    -1,
      -1,    -1,    -1,    -1,    37,    38,    39,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,
      53,    54,    55,    56,    57,    58,    59,    60,    61,    62,
       6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,
      -1,    -1,    18,    19,    -1,    21,    -1,    23,    24,    25,
      -1,    -1,    -1,    -1,    30,    31,    -1,    -1,    -1,    -1,
      -1,    37,    38,    39,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,
      56,    57,    58,    59,    60,    61,    62,     6,    -1,    -1,
       9,    10,    -1,    -1,    13,    14,    -1,    -1,    -1,    18,
      19,    -1,    21,    -1,    23,    24,    25,    -1,    -1,    -1,
      -1,    30,    31,    -1,    -1,    -1,    -1,    -1,    37,    38,
      39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    52,    53,    54,    55,    56,    57,    58,
      59,    60,    61,    62,     6,    -1,    -1,     9,    10,    -1,
      -1,    13,    14,    -1,    -1,    -1,    18,    19,    -1,    21,
      -1,    23,    24,    25,    -1,    -1,    -1,    -1,    30,    31,
      -1,    -1,    -1,    -1,    -1,    37,    38,    39,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      52,    53,    54,    55,    56,    57,    58,    59,    60,    61,
      62,     6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,
      -1,    -1,    -1,    18,    19,    -1,    21,    -1,    23,    24,
      25,    -1,    -1,    -1,    -1,    30,    31,    -1,    -1,    -1,
      -1,    -1,    37,    38,    39,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,    53,    54,
      55,    56,    57,    58,    59,    60,    61,    62,     6,    -1,
      -1,     9,    10,    -1,    -1,    13,    14,    -1,    -1,    -1,
      18,    19,    -1,    21,    -1,    23,    24,    25,    -1,    -1,
      -1,    -1,    30,    31,    -1,    -1,    -1,    -1,    -1,    37,
      38,    39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    52,    53,    54,    55,    56,    57,
      58,    59,    60,    61,    62,     6,     7,    -1,     9,    10,
      -1,    -1,    13,    14,    -1,    -1,    -1,    18,    19,    -1,
      21,    -1,    23,    24,    25,    -1,    -1,    -1,    -1,    30,
      -1,    -1,    -1,    -1,    -1,    -1,    37,    38,    39,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    52,    53,    54,    55,    56,    57,    58,    59,    60,
      61,    62,     6,    -1,    -1,     9,    10,    -1,    -1,    13,
      14,    -1,    -1,    -1,    18,    19,    -1,    21,    -1,    23,
      24,    25,    -1,    27,    -1,    -1,    30,    -1,    -1,    -1,
      -1,    -1,    -1,    37,    38,    39,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,    53,
      54,    55,    56,    57,    58,    59,    60,    61,    62,     6,
      -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,    -1,
      -1,    18,    19,    -1,    21,    -1,    23,    24,    25,    -1,
      27,    -1,    -1,    30,    -1,    -1,    -1,    -1,    -1,    -1,
      37,    38,    39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,    56,
      57,    58,    59,    60,    61,    62,     6,    -1,    -1,     9,
      10,    -1,    -1,    13,    14,    -1,    -1,    -1,    18,    19,
      -1,    21,    -1,    23,    24,    25,    -1,    27,    -1,    -1,
      30,    -1,    -1,    -1,    -1,    -1,    -1,    37,    38,    39,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    52,    53,    54,    55,    56,    57,    58,    59,
      60,    61,    62,     6,    -1,    -1,     9,    10,    -1,    -1,
      13,    14,    -1,    -1,    -1,    18,    19,    -1,    21,    -1,
      23,    24,    25,    -1,    27,    -1,    -1,    30,    -1,    -1,
      -1,    -1,    -1,    -1,    37,    38,    39,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,
      53,    54,    55,    56,    57,    58,    59,    60,    61,    62,
       6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,
      -1,    -1,    18,    19,    -1,    21,    -1,    23,    24,    25,
      -1,    -1,    -1,    -1,    30,    31,    -1,    -1,    -1,    -1,
      -1,    37,    38,    39,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,
      56,    57,    58,    59,    60,    61,    62,     6,    -1,    -1,
       9,    10,    -1,    -1,    13,    14,    -1,    -1,    -1,    18,
      19,    -1,    21,    -1,    23,    24,    25,    -1,    27,    -1,
      -1,    30,    -1,    -1,    -1,    -1,    -1,    -1,    37,    38,
      39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    52,    53,    54,    55,    56,    57,    58,
      59,    60,    61,    62,     6,    -1,    -1,     9,    10,    -1,
      -1,    13,    14,    -1,    -1,    -1,    18,    19,    -1,    21,
      -1,    23,    24,    25,    -1,    -1,    -1,    29,    30,    -1,
      -1,    -1,    -1,    -1,    -1,    37,    38,    39,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      52,    53,    54,    55,    56,    57,    58,    59,    60,    61,
      62,     6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,
      -1,    -1,    -1,    18,    19,    -1,    21,    -1,    23,    24,
      25,    -1,    -1,    -1,    -1,    30,    31,    -1,    -1,    -1,
      -1,    -1,    37,    38,    39,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,    53,    54,
      55,    56,    57,    58,    59,    60,    61,    62,     6,    -1,
      -1,     9,    10,    -1,    -1,    13,    14,    -1,    -1,    -1,
      18,    19,    -1,    21,    -1,    23,    24,    25,    -1,    -1,
      -1,    -1,    30,    31,    -1,    -1,    -1,    -1,    -1,    37,
      38,    39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    52,    53,    54,    55,    56,    57,
      58,    59,    60,    61,    62,     6,    -1,    -1,     9,    10,
      -1,    -1,    13,    14,    -1,    -1,    -1,    18,    19,    -1,
      21,    -1,    23,    24,    25,    -1,    -1,    -1,    -1,    30,
      -1,    -1,    -1,    -1,    -1,    -1,    37,    38,    39,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    52,    53,    54,    55,    56,    57,    58,    59,    60,
      61,    62,     6,    -1,    -1,     9,    10,    -1,    -1,    13,
      14,    -1,    -1,    -1,    18,    19,    -1,    21,    -1,    23,
      24,    25,    -1,    -1,    -1,    -1,    30,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    38,    39,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,    53,
      54,    55,    56,    57,    58,    59,    60,    61,    62,     6,
      -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,    -1,
      -1,    18,    19,    -1,    21,    -1,    23,    24,    25,    -1,
      -1,    -1,    -1,    30,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    39,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    52,    53,    54,    55,    56,
      57,    58,    59,    60,    61,    62,     6,    -1,    -1,     9,
      10,    -1,    -1,    13,    14,    -1,    -1,    -1,    18,    19,
      -1,    21,    -1,    23,    24,    25,    -1,    -1,    -1,    -1,
      30,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    52,    53,    54,    55,    56,    57,    58,    59,
      60,    61,    62,     6,    -1,    -1,     9,    10,    -1,    -1,
      13,    14,    -1,    -1,    -1,    18,    19,    -1,    -1,    -1,
      23,    24,    25,    -1,    -1,    -1,    -1,    30,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    52,
      53,    54,    55,    56,    57,    58,    59,    60,    61,    62,
       6,    -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,
      -1,    -1,    18,    19,    -1,    -1,    -1,    23,    24,    25,
      -1,    -1,     6,    -1,    30,     9,    10,    -1,    -1,    13,
      14,    -1,    -1,    -1,    18,    19,    -1,    -1,    -1,    23,
      24,    25,    -1,    -1,    -1,    -1,    30,    53,    54,    55,
      56,    57,    58,    59,    60,    61,    62,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      54,    55,    56,    57,    58,    59,    60,    61,    62,     6,
      -1,    -1,     9,    10,    -1,    -1,    13,    14,    -1,    -1,
      -1,    18,    19,    -1,    -1,    -1,    23,    24,    25,     6,
      -1,    -1,     9,    10,    -1,    -1,    -1,    14,    -1,    -1,
      -1,    18,    19,    -1,    -1,    -1,    23,    24,    25,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    54,    55,    56,
      57,    58,    59,    60,    61,    62,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    54,    55,    56,
      57,    58,    59,    60,    61,    62,     6,    -1,    -1,     9,
      10,    -1,    -1,    -1,    14,    -1,    -1,    -1,    18,    19,
      -1,    -1,    -1,    23,     6,    25,    -1,     9,    10,    -1,
      -1,    -1,    14,    -1,    -1,    -1,    18,    19,    -1,    -1,
      -1,    23,    -1,    25,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    54,    55,    56,    57,    58,    59,
      60,    61,    62,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    54,    55,    56,    57,    58,    59,    60,    61,
      62
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,   151,   152,     0,     3,     4,     5,     8,     9,    11,
      12,    16,    17,    18,    22,    28,    32,    33,    34,    35,
      36,    40,    63,    64,    65,    66,    67,    68,    69,    70,
      71,    72,    73,    74,    75,    79,    80,    81,    82,    83,
      85,    89,    90,    91,    92,    94,    96,    98,   101,   105,
     106,   107,   109,   110,   112,   113,   114,   118,   119,   120,
     122,   123,   124,   125,   126,   127,   132,   133,   134,   135,
     136,   137,   138,   143,   145,   153,   156,   157,   158,   165,
     166,   168,   169,   170,   172,   210,   211,   212,   219,   222,
     226,   227,   229,   230,   234,   235,   236,   237,   238,   239,
     244,   252,   254,   226,   226,   249,   226,   249,   249,   154,
      16,   226,   226,   226,   226,   226,     5,   226,   226,   226,
     226,   226,   226,   226,   226,   226,   226,    81,   212,   229,
     230,   229,   226,    81,   212,   213,   214,   237,   238,   244,
       5,   217,     5,     5,   207,   226,   157,     5,     5,     5,
       5,     5,   156,   226,   156,   226,    24,   167,   156,   210,
     226,   230,    16,   226,     5,    89,   164,    17,    83,   195,
     196,   126,   126,    83,   197,     5,     5,     5,     5,     5,
       5,   249,    81,    81,   149,     6,     9,    10,    13,    14,
      18,    19,    21,    23,    24,    25,    30,    37,    38,    39,
      52,    53,    54,    55,    56,    57,    58,    59,    60,    61,
      62,   156,    70,    71,    15,    41,    42,    43,    44,    45,
      46,    47,    48,    49,    50,    51,     5,   130,    16,    26,
      17,   238,    31,     8,    16,    26,    27,    29,    81,    83,
      84,    86,    87,    88,   130,   147,   148,   250,    11,    12,
      27,   125,   155,   157,   165,   166,   226,   226,   149,   149,
       5,   218,   130,   238,    31,   226,   226,    24,   193,   194,
     210,   230,    20,   156,    92,   226,   208,   209,   226,   210,
     230,    81,   181,   226,   156,   156,    81,   156,   156,   154,
     156,    89,   156,    16,   227,   230,    20,   156,    15,    20,
     156,   162,   163,   230,   230,   253,   230,    31,   132,   230,
     245,   246,    24,   226,   247,   248,   146,   128,   171,   128,
     173,    81,   234,   238,   244,   226,   226,   226,   226,   226,
     226,   226,   226,   226,   226,   226,   226,   226,   226,   226,
     226,   226,   226,   226,   226,   226,   226,   226,   226,   226,
     213,    24,   226,   226,   226,   226,   226,   226,   226,   226,
     226,   226,   226,   226,   193,    16,    81,   234,   241,   242,
     243,   226,   226,   240,    26,   130,    82,   226,   230,     5,
      27,    31,    81,   234,   193,   241,    31,    31,   228,   230,
      31,    20,   226,     5,    31,     4,    20,   100,   100,    15,
      20,    31,    31,     5,    27,    31,   226,   196,     9,    18,
      81,   133,   219,   220,   221,    83,    20,    31,    20,    31,
      31,   156,     5,    20,    31,   228,   131,    31,    20,   224,
      81,   212,   129,   174,   175,   212,    16,     5,     5,   238,
       7,    73,   230,    31,   226,     5,   233,    16,    26,    27,
      29,    81,    83,    84,   251,    81,    26,    27,    27,    31,
      31,   215,     7,   157,    24,   210,   230,   226,     7,   157,
     185,   208,   226,   230,    24,   177,   230,   220,    81,     7,
     157,   180,     7,    16,   182,    81,   133,   190,   191,   192,
     111,    27,   220,   220,   149,     5,    15,   163,   156,   230,
     245,   246,    15,    24,   226,    24,   226,   175,    16,    20,
     198,   193,   193,   226,   213,    27,   193,   231,   226,   240,
      29,   226,   156,   130,   216,   154,   186,   228,    31,   154,
       4,   131,   176,   230,   176,    15,   154,     4,   183,     4,
     183,    31,    20,    24,    83,     5,    81,   220,   223,   225,
     220,    31,   226,   228,   228,   131,   198,   212,    27,   108,
     115,   116,   117,   118,   119,   120,   121,   199,   201,   202,
     203,   204,   206,    31,    31,   218,    31,   130,   232,    27,
      29,    29,   241,   187,    76,    77,   188,   156,    93,   208,
     177,    31,    31,   220,    99,   183,   102,   103,   104,   183,
      27,    16,   192,    83,    15,   212,   131,    31,    20,   224,
      24,   226,    27,    81,    83,   205,   107,   204,    20,   156,
     241,    27,    76,    77,   189,     5,   157,   156,    31,     7,
     157,   179,   179,   156,   102,   156,   226,     4,     7,   184,
      27,   154,    24,    83,    15,   220,    83,   220,   220,   228,
      15,    15,    20,   156,   167,    81,   233,     5,     7,    78,
     226,     7,   157,   178,   154,   156,   184,   154,    27,    83,
      15,   220,    31,   131,   220,   220,    83,    81,    15,   226,
     154,   156,    31,   154,    97,   154,    15,   220,    16,   220,
      15,     5,   220,    31,   157,    95,   156,   220,   154,   220,
     190,     7,   156,    27,    31,   154,   111,   159,   160,   161,
      16,   156,   200,     5,   161,   154,   212,    27,    83,    31,
      16,   154,    27
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  However,
   YYFAIL appears to be in use.  Nevertheless, it is formally deprecated
   in Bison 2.4.2's NEWS entry, where a plan to phase it out is
   discussed.  */

#define YYFAIL		goto yyerrlab
#if defined YYFAIL
  /* This is here to suppress warnings from the GCC cpp's
     -Wunused-macros.  Normally we don't worry about that warning, but
     some users do, and we want to make it easy for users to remove
     YYFAIL uses, which will produce warnings from Bison 2.5.  */
#endif

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* This macro is provided for backward compatibility. */

#ifndef YY_LOCATION_PRINT
# define YY_LOCATION_PRINT(File, Loc) ((void) 0)
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (&yylval, YYLEX_PARAM)
#else
# define YYLEX yylex (&yylval)
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
#else
static void
yy_stack_print (yybottom, yytop)
    yytype_int16 *yybottom;
    yytype_int16 *yytop;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yyrule)
    YYSTYPE *yyvsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       		       );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif


#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into *YYMSG, which is of size *YYMSG_ALLOC, an error message
   about the unexpected token YYTOKEN for the state stack whose top is
   YYSSP.

   Return 0 if *YYMSG was successfully written.  Return 1 if *YYMSG is
   not large enough to hold the message.  In that case, also set
   *YYMSG_ALLOC to the required number of bytes.  Return 2 if the
   required number of bytes is too large to store.  */
static int
yysyntax_error (YYSIZE_T *yymsg_alloc, char **yymsg,
                yytype_int16 *yyssp, int yytoken)
{
  YYSIZE_T yysize0 = yytnamerr (0, yytname[yytoken]);
  YYSIZE_T yysize = yysize0;
  YYSIZE_T yysize1;
  enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
  /* Internationalized format string. */
  const char *yyformat = 0;
  /* Arguments of yyformat. */
  char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
  /* Number of reported tokens (one for the "unexpected", one per
     "expected"). */
  int yycount = 0;

  /* There are many possibilities here to consider:
     - Assume YYFAIL is not used.  It's too flawed to consider.  See
       <http://lists.gnu.org/archive/html/bison-patches/2009-12/msg00024.html>
       for details.  YYERROR is fine as it does not invoke this
       function.
     - If this state is a consistent state with a default action, then
       the only way this function was invoked is if the default action
       is an error action.  In that case, don't check for expected
       tokens because there are none.
     - The only way there can be no lookahead present (in yychar) is if
       this state is a consistent state with a default action.  Thus,
       detecting the absence of a lookahead is sufficient to determine
       that there is no unexpected or expected token to report.  In that
       case, just report a simple "syntax error".
     - Don't assume there isn't a lookahead just because this state is a
       consistent state with a default action.  There might have been a
       previous inconsistent state, consistent state with a non-default
       action, or user semantic action that manipulated yychar.
     - Of course, the expected token list depends on states to have
       correct lookahead information, and it depends on the parser not
       to perform extra reductions after fetching a lookahead from the
       scanner and before detecting a syntax error.  Thus, state merging
       (from LALR or IELR) and default reductions corrupt the expected
       token list.  However, the list is correct for canonical LR with
       one exception: it will still contain any token that will not be
       accepted due to an error action in a later state.
  */
  if (yytoken != YYEMPTY)
    {
      int yyn = yypact[*yyssp];
      yyarg[yycount++] = yytname[yytoken];
      if (!yypact_value_is_default (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative indexes in
             YYCHECK.  In other words, skip the first -YYN actions for
             this state because they are default actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST - yyn + 1;
          int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
          int yyx;

          for (yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR
                && !yytable_value_is_error (yytable[yyx + yyn]))
              {
                if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
                  {
                    yycount = 1;
                    yysize = yysize0;
                    break;
                  }
                yyarg[yycount++] = yytname[yyx];
                yysize1 = yysize + yytnamerr (0, yytname[yyx]);
                if (! (yysize <= yysize1
                       && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
                  return 2;
                yysize = yysize1;
              }
        }
    }

  switch (yycount)
    {
# define YYCASE_(N, S)                      \
      case N:                               \
        yyformat = S;                       \
      break
      YYCASE_(0, YY_("syntax error"));
      YYCASE_(1, YY_("syntax error, unexpected %s"));
      YYCASE_(2, YY_("syntax error, unexpected %s, expecting %s"));
      YYCASE_(3, YY_("syntax error, unexpected %s, expecting %s or %s"));
      YYCASE_(4, YY_("syntax error, unexpected %s, expecting %s or %s or %s"));
      YYCASE_(5, YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s"));
# undef YYCASE_
    }

  yysize1 = yysize + yystrlen (yyformat);
  if (! (yysize <= yysize1 && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
    return 2;
  yysize = yysize1;

  if (*yymsg_alloc < yysize)
    {
      *yymsg_alloc = 2 * yysize;
      if (! (yysize <= *yymsg_alloc
             && *yymsg_alloc <= YYSTACK_ALLOC_MAXIMUM))
        *yymsg_alloc = YYSTACK_ALLOC_MAXIMUM;
      return 1;
    }

  /* Avoid sprintf, as that infringes on the user's name space.
     Don't have undefined behavior even if the translation
     produced a string with the wrong number of "%s"s.  */
  {
    char *yyp = *yymsg;
    int yyi = 0;
    while ((*yyp = *yyformat) != '\0')
      if (*yyp == '%' && yyformat[1] == 's' && yyi < yycount)
        {
          yyp += yytnamerr (yyp, yyarg[yyi++]);
          yyformat += 2;
        }
      else
        {
          yyp++;
          yyformat++;
        }
  }
  return 0;
}
#endif /* YYERROR_VERBOSE */

/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
#else
static void
yydestruct (yymsg, yytype, yyvaluep)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
#endif
{
  YYUSE (yyvaluep);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}


/* Prevent warnings from -Wmissing-prototypes.  */
#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */


/*----------.
| yyparse.  |
`----------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{
/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;

    /* Number of syntax errors so far.  */
    int yynerrs;

    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       `yyss': related to states.
       `yyvs': related to semantic values.

       Refer to the stacks thru separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yytoken = 0;
  yyss = yyssa;
  yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */
  yyssp = yyss;
  yyvsp = yyvs;

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;

	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),
		    &yystacksize);

	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss_alloc, yyss);
	YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 2:

/* Line 1806 of yacc.c  */
#line 371 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 55 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);
root= (yyval.t);

    }
    break;

  case 3:

/* Line 1806 of yacc.c  */
#line 383 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 65 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 4:

/* Line 1806 of yacc.c  */
#line 400 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 65 );

    }
    break;

  case 5:

/* Line 1806 of yacc.c  */
#line 407 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 42 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 6:

/* Line 1806 of yacc.c  */
#line 418 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 42 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 7:

/* Line 1806 of yacc.c  */
#line 429 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 42 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 8:

/* Line 1806 of yacc.c  */
#line 440 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 42 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 9:

/* Line 1806 of yacc.c  */
#line 469 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 27 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 10:

/* Line 1806 of yacc.c  */
#line 486 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 27 );

    }
    break;

  case 11:

/* Line 1806 of yacc.c  */
#line 493 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 17 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 12:

/* Line 1806 of yacc.c  */
#line 504 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 17 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 13:

/* Line 1806 of yacc.c  */
#line 515 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 17 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 14:

/* Line 1806 of yacc.c  */
#line 526 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 17 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 15:

/* Line 1806 of yacc.c  */
#line 555 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 59 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 16:

/* Line 1806 of yacc.c  */
#line 566 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 59 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 17:

/* Line 1806 of yacc.c  */
#line 577 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 57 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 18:

/* Line 1806 of yacc.c  */
#line 588 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 19:

/* Line 1806 of yacc.c  */
#line 611 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (7)].t));

        (yyvsp[(1) - (7)].t)->parent= (yyval.t);

        (yyvsp[(1) - (7)].t)->nextSibbling= (yyvsp[(2) - (7)].t);

        (yyval.t)->addChild((yyvsp[(2) - (7)].t));

        (yyvsp[(2) - (7)].t)->parent= (yyval.t);

        (yyvsp[(2) - (7)].t)->nextSibbling= (yyvsp[(3) - (7)].t);

        (yyval.t)->addChild((yyvsp[(3) - (7)].t));

        (yyvsp[(3) - (7)].t)->parent= (yyval.t);

        (yyvsp[(3) - (7)].t)->nextSibbling= (yyvsp[(4) - (7)].t);

        (yyval.t)->addChild((yyvsp[(4) - (7)].t));

        (yyvsp[(4) - (7)].t)->parent= (yyval.t);

        (yyvsp[(4) - (7)].t)->nextSibbling= (yyvsp[(5) - (7)].t);

        (yyval.t)->addChild((yyvsp[(5) - (7)].t));

        (yyvsp[(5) - (7)].t)->parent= (yyval.t);

        (yyvsp[(5) - (7)].t)->nextSibbling= (yyvsp[(6) - (7)].t);

        (yyval.t)->addChild((yyvsp[(6) - (7)].t));

        (yyvsp[(6) - (7)].t)->parent= (yyval.t);

        (yyvsp[(6) - (7)].t)->nextSibbling= (yyvsp[(7) - (7)].t);

        (yyval.t)->addChild((yyvsp[(7) - (7)].t));

        (yyvsp[(7) - (7)].t)->parent= (yyval.t);

    }
    break;

  case 20:

/* Line 1806 of yacc.c  */
#line 658 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (10)].t));

        (yyvsp[(1) - (10)].t)->parent= (yyval.t);

        (yyvsp[(1) - (10)].t)->nextSibbling= (yyvsp[(2) - (10)].t);

        (yyval.t)->addChild((yyvsp[(2) - (10)].t));

        (yyvsp[(2) - (10)].t)->parent= (yyval.t);

        (yyvsp[(2) - (10)].t)->nextSibbling= (yyvsp[(3) - (10)].t);

        (yyval.t)->addChild((yyvsp[(3) - (10)].t));

        (yyvsp[(3) - (10)].t)->parent= (yyval.t);

        (yyvsp[(3) - (10)].t)->nextSibbling= (yyvsp[(4) - (10)].t);

        (yyval.t)->addChild((yyvsp[(4) - (10)].t));

        (yyvsp[(4) - (10)].t)->parent= (yyval.t);

        (yyvsp[(4) - (10)].t)->nextSibbling= (yyvsp[(5) - (10)].t);

        (yyval.t)->addChild((yyvsp[(5) - (10)].t));

        (yyvsp[(5) - (10)].t)->parent= (yyval.t);

        (yyvsp[(5) - (10)].t)->nextSibbling= (yyvsp[(6) - (10)].t);

        (yyval.t)->addChild((yyvsp[(6) - (10)].t));

        (yyvsp[(6) - (10)].t)->parent= (yyval.t);

        (yyvsp[(6) - (10)].t)->nextSibbling= (yyvsp[(7) - (10)].t);

        (yyval.t)->addChild((yyvsp[(7) - (10)].t));

        (yyvsp[(7) - (10)].t)->parent= (yyval.t);

        (yyvsp[(7) - (10)].t)->nextSibbling= (yyvsp[(8) - (10)].t);

        (yyval.t)->addChild((yyvsp[(8) - (10)].t));

        (yyvsp[(8) - (10)].t)->parent= (yyval.t);

        (yyvsp[(8) - (10)].t)->nextSibbling= (yyvsp[(9) - (10)].t);

        (yyval.t)->addChild((yyvsp[(9) - (10)].t));

        (yyvsp[(9) - (10)].t)->parent= (yyval.t);

        (yyvsp[(9) - (10)].t)->nextSibbling= (yyvsp[(10) - (10)].t);

        (yyval.t)->addChild((yyvsp[(10) - (10)].t));

        (yyvsp[(10) - (10)].t)->parent= (yyval.t);

    }
    break;

  case 21:

/* Line 1806 of yacc.c  */
#line 723 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 22:

/* Line 1806 of yacc.c  */
#line 758 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (7)].t));

        (yyvsp[(1) - (7)].t)->parent= (yyval.t);

        (yyvsp[(1) - (7)].t)->nextSibbling= (yyvsp[(2) - (7)].t);

        (yyval.t)->addChild((yyvsp[(2) - (7)].t));

        (yyvsp[(2) - (7)].t)->parent= (yyval.t);

        (yyvsp[(2) - (7)].t)->nextSibbling= (yyvsp[(3) - (7)].t);

        (yyval.t)->addChild((yyvsp[(3) - (7)].t));

        (yyvsp[(3) - (7)].t)->parent= (yyval.t);

        (yyvsp[(3) - (7)].t)->nextSibbling= (yyvsp[(4) - (7)].t);

        (yyval.t)->addChild((yyvsp[(4) - (7)].t));

        (yyvsp[(4) - (7)].t)->parent= (yyval.t);

        (yyvsp[(4) - (7)].t)->nextSibbling= (yyvsp[(5) - (7)].t);

        (yyval.t)->addChild((yyvsp[(5) - (7)].t));

        (yyvsp[(5) - (7)].t)->parent= (yyval.t);

        (yyvsp[(5) - (7)].t)->nextSibbling= (yyvsp[(6) - (7)].t);

        (yyval.t)->addChild((yyvsp[(6) - (7)].t));

        (yyvsp[(6) - (7)].t)->parent= (yyval.t);

        (yyvsp[(6) - (7)].t)->nextSibbling= (yyvsp[(7) - (7)].t);

        (yyval.t)->addChild((yyvsp[(7) - (7)].t));

        (yyvsp[(7) - (7)].t)->parent= (yyval.t);

    }
    break;

  case 23:

/* Line 1806 of yacc.c  */
#line 805 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (9)].t));

        (yyvsp[(1) - (9)].t)->parent= (yyval.t);

        (yyvsp[(1) - (9)].t)->nextSibbling= (yyvsp[(2) - (9)].t);

        (yyval.t)->addChild((yyvsp[(2) - (9)].t));

        (yyvsp[(2) - (9)].t)->parent= (yyval.t);

        (yyvsp[(2) - (9)].t)->nextSibbling= (yyvsp[(3) - (9)].t);

        (yyval.t)->addChild((yyvsp[(3) - (9)].t));

        (yyvsp[(3) - (9)].t)->parent= (yyval.t);

        (yyvsp[(3) - (9)].t)->nextSibbling= (yyvsp[(4) - (9)].t);

        (yyval.t)->addChild((yyvsp[(4) - (9)].t));

        (yyvsp[(4) - (9)].t)->parent= (yyval.t);

        (yyvsp[(4) - (9)].t)->nextSibbling= (yyvsp[(5) - (9)].t);

        (yyval.t)->addChild((yyvsp[(5) - (9)].t));

        (yyvsp[(5) - (9)].t)->parent= (yyval.t);

        (yyvsp[(5) - (9)].t)->nextSibbling= (yyvsp[(6) - (9)].t);

        (yyval.t)->addChild((yyvsp[(6) - (9)].t));

        (yyvsp[(6) - (9)].t)->parent= (yyval.t);

        (yyvsp[(6) - (9)].t)->nextSibbling= (yyvsp[(7) - (9)].t);

        (yyval.t)->addChild((yyvsp[(7) - (9)].t));

        (yyvsp[(7) - (9)].t)->parent= (yyval.t);

        (yyvsp[(7) - (9)].t)->nextSibbling= (yyvsp[(8) - (9)].t);

        (yyval.t)->addChild((yyvsp[(8) - (9)].t));

        (yyvsp[(8) - (9)].t)->parent= (yyval.t);

        (yyvsp[(8) - (9)].t)->nextSibbling= (yyvsp[(9) - (9)].t);

        (yyval.t)->addChild((yyvsp[(9) - (9)].t));

        (yyvsp[(9) - (9)].t)->parent= (yyval.t);

    }
    break;

  case 24:

/* Line 1806 of yacc.c  */
#line 864 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 25:

/* Line 1806 of yacc.c  */
#line 899 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 26:

/* Line 1806 of yacc.c  */
#line 916 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 27:

/* Line 1806 of yacc.c  */
#line 939 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 28:

/* Line 1806 of yacc.c  */
#line 956 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 29:

/* Line 1806 of yacc.c  */
#line 979 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 30:

/* Line 1806 of yacc.c  */
#line 996 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 31:

/* Line 1806 of yacc.c  */
#line 1019 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 32:

/* Line 1806 of yacc.c  */
#line 1042 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 33:

/* Line 1806 of yacc.c  */
#line 1065 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 34:

/* Line 1806 of yacc.c  */
#line 1088 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 35:

/* Line 1806 of yacc.c  */
#line 1111 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 36:

/* Line 1806 of yacc.c  */
#line 1122 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 37:

/* Line 1806 of yacc.c  */
#line 1139 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 38:

/* Line 1806 of yacc.c  */
#line 1162 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 39:

/* Line 1806 of yacc.c  */
#line 1197 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (8)].t));

        (yyvsp[(1) - (8)].t)->parent= (yyval.t);

        (yyvsp[(1) - (8)].t)->nextSibbling= (yyvsp[(2) - (8)].t);

        (yyval.t)->addChild((yyvsp[(2) - (8)].t));

        (yyvsp[(2) - (8)].t)->parent= (yyval.t);

        (yyvsp[(2) - (8)].t)->nextSibbling= (yyvsp[(3) - (8)].t);

        (yyval.t)->addChild((yyvsp[(3) - (8)].t));

        (yyvsp[(3) - (8)].t)->parent= (yyval.t);

        (yyvsp[(3) - (8)].t)->nextSibbling= (yyvsp[(4) - (8)].t);

        (yyval.t)->addChild((yyvsp[(4) - (8)].t));

        (yyvsp[(4) - (8)].t)->parent= (yyval.t);

        (yyvsp[(4) - (8)].t)->nextSibbling= (yyvsp[(5) - (8)].t);

        (yyval.t)->addChild((yyvsp[(5) - (8)].t));

        (yyvsp[(5) - (8)].t)->parent= (yyval.t);

        (yyvsp[(5) - (8)].t)->nextSibbling= (yyvsp[(6) - (8)].t);

        (yyval.t)->addChild((yyvsp[(6) - (8)].t));

        (yyvsp[(6) - (8)].t)->parent= (yyval.t);

        (yyvsp[(6) - (8)].t)->nextSibbling= (yyvsp[(7) - (8)].t);

        (yyval.t)->addChild((yyvsp[(7) - (8)].t));

        (yyvsp[(7) - (8)].t)->parent= (yyval.t);

        (yyvsp[(7) - (8)].t)->nextSibbling= (yyvsp[(8) - (8)].t);

        (yyval.t)->addChild((yyvsp[(8) - (8)].t));

        (yyvsp[(8) - (8)].t)->parent= (yyval.t);

    }
    break;

  case 40:

/* Line 1806 of yacc.c  */
#line 1250 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (8)].t));

        (yyvsp[(1) - (8)].t)->parent= (yyval.t);

        (yyvsp[(1) - (8)].t)->nextSibbling= (yyvsp[(2) - (8)].t);

        (yyval.t)->addChild((yyvsp[(2) - (8)].t));

        (yyvsp[(2) - (8)].t)->parent= (yyval.t);

        (yyvsp[(2) - (8)].t)->nextSibbling= (yyvsp[(3) - (8)].t);

        (yyval.t)->addChild((yyvsp[(3) - (8)].t));

        (yyvsp[(3) - (8)].t)->parent= (yyval.t);

        (yyvsp[(3) - (8)].t)->nextSibbling= (yyvsp[(4) - (8)].t);

        (yyval.t)->addChild((yyvsp[(4) - (8)].t));

        (yyvsp[(4) - (8)].t)->parent= (yyval.t);

        (yyvsp[(4) - (8)].t)->nextSibbling= (yyvsp[(5) - (8)].t);

        (yyval.t)->addChild((yyvsp[(5) - (8)].t));

        (yyvsp[(5) - (8)].t)->parent= (yyval.t);

        (yyvsp[(5) - (8)].t)->nextSibbling= (yyvsp[(6) - (8)].t);

        (yyval.t)->addChild((yyvsp[(6) - (8)].t));

        (yyvsp[(6) - (8)].t)->parent= (yyval.t);

        (yyvsp[(6) - (8)].t)->nextSibbling= (yyvsp[(7) - (8)].t);

        (yyval.t)->addChild((yyvsp[(7) - (8)].t));

        (yyvsp[(7) - (8)].t)->parent= (yyval.t);

        (yyvsp[(7) - (8)].t)->nextSibbling= (yyvsp[(8) - (8)].t);

        (yyval.t)->addChild((yyvsp[(8) - (8)].t));

        (yyvsp[(8) - (8)].t)->parent= (yyval.t);

    }
    break;

  case 41:

/* Line 1806 of yacc.c  */
#line 1303 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 42:

/* Line 1806 of yacc.c  */
#line 1338 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 43:

/* Line 1806 of yacc.c  */
#line 1349 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (13)].t));

        (yyvsp[(1) - (13)].t)->parent= (yyval.t);

        (yyvsp[(1) - (13)].t)->nextSibbling= (yyvsp[(2) - (13)].t);

        (yyval.t)->addChild((yyvsp[(2) - (13)].t));

        (yyvsp[(2) - (13)].t)->parent= (yyval.t);

        (yyvsp[(2) - (13)].t)->nextSibbling= (yyvsp[(3) - (13)].t);

        (yyval.t)->addChild((yyvsp[(3) - (13)].t));

        (yyvsp[(3) - (13)].t)->parent= (yyval.t);

        (yyvsp[(3) - (13)].t)->nextSibbling= (yyvsp[(4) - (13)].t);

        (yyval.t)->addChild((yyvsp[(4) - (13)].t));

        (yyvsp[(4) - (13)].t)->parent= (yyval.t);

        (yyvsp[(4) - (13)].t)->nextSibbling= (yyvsp[(5) - (13)].t);

        (yyval.t)->addChild((yyvsp[(5) - (13)].t));

        (yyvsp[(5) - (13)].t)->parent= (yyval.t);

        (yyvsp[(5) - (13)].t)->nextSibbling= (yyvsp[(6) - (13)].t);

        (yyval.t)->addChild((yyvsp[(6) - (13)].t));

        (yyvsp[(6) - (13)].t)->parent= (yyval.t);

        (yyvsp[(6) - (13)].t)->nextSibbling= (yyvsp[(7) - (13)].t);

        (yyval.t)->addChild((yyvsp[(7) - (13)].t));

        (yyvsp[(7) - (13)].t)->parent= (yyval.t);

        (yyvsp[(7) - (13)].t)->nextSibbling= (yyvsp[(8) - (13)].t);

        (yyval.t)->addChild((yyvsp[(8) - (13)].t));

        (yyvsp[(8) - (13)].t)->parent= (yyval.t);

        (yyvsp[(8) - (13)].t)->nextSibbling= (yyvsp[(9) - (13)].t);

        (yyval.t)->addChild((yyvsp[(9) - (13)].t));

        (yyvsp[(9) - (13)].t)->parent= (yyval.t);

        (yyvsp[(9) - (13)].t)->nextSibbling= (yyvsp[(10) - (13)].t);

        (yyval.t)->addChild((yyvsp[(10) - (13)].t));

        (yyvsp[(10) - (13)].t)->parent= (yyval.t);

        (yyvsp[(10) - (13)].t)->nextSibbling= (yyvsp[(11) - (13)].t);

        (yyval.t)->addChild((yyvsp[(11) - (13)].t));

        (yyvsp[(11) - (13)].t)->parent= (yyval.t);

        (yyvsp[(11) - (13)].t)->nextSibbling= (yyvsp[(12) - (13)].t);

        (yyval.t)->addChild((yyvsp[(12) - (13)].t));

        (yyvsp[(12) - (13)].t)->parent= (yyval.t);

        (yyvsp[(12) - (13)].t)->nextSibbling= (yyvsp[(13) - (13)].t);

        (yyval.t)->addChild((yyvsp[(13) - (13)].t));

        (yyvsp[(13) - (13)].t)->parent= (yyval.t);

    }
    break;

  case 44:

/* Line 1806 of yacc.c  */
#line 1432 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 28 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 45:

/* Line 1806 of yacc.c  */
#line 1455 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 92 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 46:

/* Line 1806 of yacc.c  */
#line 1466 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 92 );

    }
    break;

  case 47:

/* Line 1806 of yacc.c  */
#line 1473 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 87 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 48:

/* Line 1806 of yacc.c  */
#line 1484 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 87 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 49:

/* Line 1806 of yacc.c  */
#line 1501 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 8 );

        (yyval.t)->addChild((yyvsp[(1) - (8)].t));

        (yyvsp[(1) - (8)].t)->parent= (yyval.t);

        (yyvsp[(1) - (8)].t)->nextSibbling= (yyvsp[(2) - (8)].t);

        (yyval.t)->addChild((yyvsp[(2) - (8)].t));

        (yyvsp[(2) - (8)].t)->parent= (yyval.t);

        (yyvsp[(2) - (8)].t)->nextSibbling= (yyvsp[(3) - (8)].t);

        (yyval.t)->addChild((yyvsp[(3) - (8)].t));

        (yyvsp[(3) - (8)].t)->parent= (yyval.t);

        (yyvsp[(3) - (8)].t)->nextSibbling= (yyvsp[(4) - (8)].t);

        (yyval.t)->addChild((yyvsp[(4) - (8)].t));

        (yyvsp[(4) - (8)].t)->parent= (yyval.t);

        (yyvsp[(4) - (8)].t)->nextSibbling= (yyvsp[(5) - (8)].t);

        (yyval.t)->addChild((yyvsp[(5) - (8)].t));

        (yyvsp[(5) - (8)].t)->parent= (yyval.t);

        (yyvsp[(5) - (8)].t)->nextSibbling= (yyvsp[(6) - (8)].t);

        (yyval.t)->addChild((yyvsp[(6) - (8)].t));

        (yyvsp[(6) - (8)].t)->parent= (yyval.t);

        (yyvsp[(6) - (8)].t)->nextSibbling= (yyvsp[(7) - (8)].t);

        (yyval.t)->addChild((yyvsp[(7) - (8)].t));

        (yyvsp[(7) - (8)].t)->parent= (yyval.t);

        (yyvsp[(7) - (8)].t)->nextSibbling= (yyvsp[(8) - (8)].t);

        (yyval.t)->addChild((yyvsp[(8) - (8)].t));

        (yyvsp[(8) - (8)].t)->parent= (yyval.t);

    }
    break;

  case 50:

/* Line 1806 of yacc.c  */
#line 1554 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 4 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 51:

/* Line 1806 of yacc.c  */
#line 1565 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 4 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 52:

/* Line 1806 of yacc.c  */
#line 1588 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 62 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 53:

/* Line 1806 of yacc.c  */
#line 1599 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 49 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 54:

/* Line 1806 of yacc.c  */
#line 1610 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 49 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 55:

/* Line 1806 of yacc.c  */
#line 1633 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 56 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 56:

/* Line 1806 of yacc.c  */
#line 1644 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 33 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 57:

/* Line 1806 of yacc.c  */
#line 1655 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 64 );

    }
    break;

  case 58:

/* Line 1806 of yacc.c  */
#line 1662 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 64 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 59:

/* Line 1806 of yacc.c  */
#line 1673 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 77 );

        (yyval.t)->addChild((yyvsp[(1) - (9)].t));

        (yyvsp[(1) - (9)].t)->parent= (yyval.t);

        (yyvsp[(1) - (9)].t)->nextSibbling= (yyvsp[(2) - (9)].t);

        (yyval.t)->addChild((yyvsp[(2) - (9)].t));

        (yyvsp[(2) - (9)].t)->parent= (yyval.t);

        (yyvsp[(2) - (9)].t)->nextSibbling= (yyvsp[(3) - (9)].t);

        (yyval.t)->addChild((yyvsp[(3) - (9)].t));

        (yyvsp[(3) - (9)].t)->parent= (yyval.t);

        (yyvsp[(3) - (9)].t)->nextSibbling= (yyvsp[(4) - (9)].t);

        (yyval.t)->addChild((yyvsp[(4) - (9)].t));

        (yyvsp[(4) - (9)].t)->parent= (yyval.t);

        (yyvsp[(4) - (9)].t)->nextSibbling= (yyvsp[(5) - (9)].t);

        (yyval.t)->addChild((yyvsp[(5) - (9)].t));

        (yyvsp[(5) - (9)].t)->parent= (yyval.t);

        (yyvsp[(5) - (9)].t)->nextSibbling= (yyvsp[(6) - (9)].t);

        (yyval.t)->addChild((yyvsp[(6) - (9)].t));

        (yyvsp[(6) - (9)].t)->parent= (yyval.t);

        (yyvsp[(6) - (9)].t)->nextSibbling= (yyvsp[(7) - (9)].t);

        (yyval.t)->addChild((yyvsp[(7) - (9)].t));

        (yyvsp[(7) - (9)].t)->parent= (yyval.t);

        (yyvsp[(7) - (9)].t)->nextSibbling= (yyvsp[(8) - (9)].t);

        (yyval.t)->addChild((yyvsp[(8) - (9)].t));

        (yyvsp[(8) - (9)].t)->parent= (yyval.t);

        (yyvsp[(8) - (9)].t)->nextSibbling= (yyvsp[(9) - (9)].t);

        (yyval.t)->addChild((yyvsp[(9) - (9)].t));

        (yyvsp[(9) - (9)].t)->parent= (yyval.t);

    }
    break;

  case 60:

/* Line 1806 of yacc.c  */
#line 1732 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 35 );

        (yyval.t)->addChild((yyvsp[(1) - (7)].t));

        (yyvsp[(1) - (7)].t)->parent= (yyval.t);

        (yyvsp[(1) - (7)].t)->nextSibbling= (yyvsp[(2) - (7)].t);

        (yyval.t)->addChild((yyvsp[(2) - (7)].t));

        (yyvsp[(2) - (7)].t)->parent= (yyval.t);

        (yyvsp[(2) - (7)].t)->nextSibbling= (yyvsp[(3) - (7)].t);

        (yyval.t)->addChild((yyvsp[(3) - (7)].t));

        (yyvsp[(3) - (7)].t)->parent= (yyval.t);

        (yyvsp[(3) - (7)].t)->nextSibbling= (yyvsp[(4) - (7)].t);

        (yyval.t)->addChild((yyvsp[(4) - (7)].t));

        (yyvsp[(4) - (7)].t)->parent= (yyval.t);

        (yyvsp[(4) - (7)].t)->nextSibbling= (yyvsp[(5) - (7)].t);

        (yyval.t)->addChild((yyvsp[(5) - (7)].t));

        (yyvsp[(5) - (7)].t)->parent= (yyval.t);

        (yyvsp[(5) - (7)].t)->nextSibbling= (yyvsp[(6) - (7)].t);

        (yyval.t)->addChild((yyvsp[(6) - (7)].t));

        (yyvsp[(6) - (7)].t)->parent= (yyval.t);

        (yyvsp[(6) - (7)].t)->nextSibbling= (yyvsp[(7) - (7)].t);

        (yyval.t)->addChild((yyvsp[(7) - (7)].t));

        (yyvsp[(7) - (7)].t)->parent= (yyval.t);

    }
    break;

  case 61:

/* Line 1806 of yacc.c  */
#line 1779 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 35 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 62:

/* Line 1806 of yacc.c  */
#line 1820 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 45 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 63:

/* Line 1806 of yacc.c  */
#line 1831 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 45 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 64:

/* Line 1806 of yacc.c  */
#line 1848 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 45 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 65:

/* Line 1806 of yacc.c  */
#line 1865 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 80 );

    }
    break;

  case 66:

/* Line 1806 of yacc.c  */
#line 1872 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 80 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 67:

/* Line 1806 of yacc.c  */
#line 1889 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 66 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 68:

/* Line 1806 of yacc.c  */
#line 1900 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 38 );

    }
    break;

  case 69:

/* Line 1806 of yacc.c  */
#line 1907 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 38 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 70:

/* Line 1806 of yacc.c  */
#line 1924 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 30 );

    }
    break;

  case 71:

/* Line 1806 of yacc.c  */
#line 1931 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 30 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 72:

/* Line 1806 of yacc.c  */
#line 1948 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 41 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 73:

/* Line 1806 of yacc.c  */
#line 1959 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 41 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 74:

/* Line 1806 of yacc.c  */
#line 1982 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 91 );

    }
    break;

  case 75:

/* Line 1806 of yacc.c  */
#line 1989 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 91 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 76:

/* Line 1806 of yacc.c  */
#line 2006 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 0 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 77:

/* Line 1806 of yacc.c  */
#line 2017 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 0 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 78:

/* Line 1806 of yacc.c  */
#line 2034 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 7 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 79:

/* Line 1806 of yacc.c  */
#line 2045 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 7 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 80:

/* Line 1806 of yacc.c  */
#line 2074 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 85 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 81:

/* Line 1806 of yacc.c  */
#line 2085 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 85 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 82:

/* Line 1806 of yacc.c  */
#line 2114 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 22 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 83:

/* Line 1806 of yacc.c  */
#line 2125 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 22 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 84:

/* Line 1806 of yacc.c  */
#line 2154 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 95 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 85:

/* Line 1806 of yacc.c  */
#line 2177 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 95 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 86:

/* Line 1806 of yacc.c  */
#line 2212 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 75 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 87:

/* Line 1806 of yacc.c  */
#line 2235 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 75 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 88:

/* Line 1806 of yacc.c  */
#line 2264 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 75 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 89:

/* Line 1806 of yacc.c  */
#line 2293 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 75 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 90:

/* Line 1806 of yacc.c  */
#line 2328 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 48 );

    }
    break;

  case 91:

/* Line 1806 of yacc.c  */
#line 2335 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 48 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 92:

/* Line 1806 of yacc.c  */
#line 2370 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 48 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 93:

/* Line 1806 of yacc.c  */
#line 2399 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 93 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 94:

/* Line 1806 of yacc.c  */
#line 2410 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 93 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 95:

/* Line 1806 of yacc.c  */
#line 2421 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 5 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 96:

/* Line 1806 of yacc.c  */
#line 2432 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 5 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 97:

/* Line 1806 of yacc.c  */
#line 2461 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 46 );

    }
    break;

  case 98:

/* Line 1806 of yacc.c  */
#line 2468 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 46 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 99:

/* Line 1806 of yacc.c  */
#line 2509 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 79 );

    }
    break;

  case 100:

/* Line 1806 of yacc.c  */
#line 2516 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 79 );

        (yyval.t)->addChild((yyvsp[(1) - (7)].t));

        (yyvsp[(1) - (7)].t)->parent= (yyval.t);

        (yyvsp[(1) - (7)].t)->nextSibbling= (yyvsp[(2) - (7)].t);

        (yyval.t)->addChild((yyvsp[(2) - (7)].t));

        (yyvsp[(2) - (7)].t)->parent= (yyval.t);

        (yyvsp[(2) - (7)].t)->nextSibbling= (yyvsp[(3) - (7)].t);

        (yyval.t)->addChild((yyvsp[(3) - (7)].t));

        (yyvsp[(3) - (7)].t)->parent= (yyval.t);

        (yyvsp[(3) - (7)].t)->nextSibbling= (yyvsp[(4) - (7)].t);

        (yyval.t)->addChild((yyvsp[(4) - (7)].t));

        (yyvsp[(4) - (7)].t)->parent= (yyval.t);

        (yyvsp[(4) - (7)].t)->nextSibbling= (yyvsp[(5) - (7)].t);

        (yyval.t)->addChild((yyvsp[(5) - (7)].t));

        (yyvsp[(5) - (7)].t)->parent= (yyval.t);

        (yyvsp[(5) - (7)].t)->nextSibbling= (yyvsp[(6) - (7)].t);

        (yyval.t)->addChild((yyvsp[(6) - (7)].t));

        (yyvsp[(6) - (7)].t)->parent= (yyval.t);

        (yyvsp[(6) - (7)].t)->nextSibbling= (yyvsp[(7) - (7)].t);

        (yyval.t)->addChild((yyvsp[(7) - (7)].t));

        (yyvsp[(7) - (7)].t)->parent= (yyval.t);

    }
    break;

  case 101:

/* Line 1806 of yacc.c  */
#line 2563 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 25 );

    }
    break;

  case 102:

/* Line 1806 of yacc.c  */
#line 2570 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 25 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 103:

/* Line 1806 of yacc.c  */
#line 2587 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 69 );

    }
    break;

  case 104:

/* Line 1806 of yacc.c  */
#line 2594 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 69 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 105:

/* Line 1806 of yacc.c  */
#line 2617 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 12 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 106:

/* Line 1806 of yacc.c  */
#line 2628 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 12 );

    }
    break;

  case 107:

/* Line 1806 of yacc.c  */
#line 2635 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 108:

/* Line 1806 of yacc.c  */
#line 2652 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 109:

/* Line 1806 of yacc.c  */
#line 2675 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 110:

/* Line 1806 of yacc.c  */
#line 2710 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 111:

/* Line 1806 of yacc.c  */
#line 2739 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 112:

/* Line 1806 of yacc.c  */
#line 2768 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 113:

/* Line 1806 of yacc.c  */
#line 2803 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (7)].t));

        (yyvsp[(1) - (7)].t)->parent= (yyval.t);

        (yyvsp[(1) - (7)].t)->nextSibbling= (yyvsp[(2) - (7)].t);

        (yyval.t)->addChild((yyvsp[(2) - (7)].t));

        (yyvsp[(2) - (7)].t)->parent= (yyval.t);

        (yyvsp[(2) - (7)].t)->nextSibbling= (yyvsp[(3) - (7)].t);

        (yyval.t)->addChild((yyvsp[(3) - (7)].t));

        (yyvsp[(3) - (7)].t)->parent= (yyval.t);

        (yyvsp[(3) - (7)].t)->nextSibbling= (yyvsp[(4) - (7)].t);

        (yyval.t)->addChild((yyvsp[(4) - (7)].t));

        (yyvsp[(4) - (7)].t)->parent= (yyval.t);

        (yyvsp[(4) - (7)].t)->nextSibbling= (yyvsp[(5) - (7)].t);

        (yyval.t)->addChild((yyvsp[(5) - (7)].t));

        (yyvsp[(5) - (7)].t)->parent= (yyval.t);

        (yyvsp[(5) - (7)].t)->nextSibbling= (yyvsp[(6) - (7)].t);

        (yyval.t)->addChild((yyvsp[(6) - (7)].t));

        (yyvsp[(6) - (7)].t)->parent= (yyval.t);

        (yyvsp[(6) - (7)].t)->nextSibbling= (yyvsp[(7) - (7)].t);

        (yyval.t)->addChild((yyvsp[(7) - (7)].t));

        (yyvsp[(7) - (7)].t)->parent= (yyval.t);

    }
    break;

  case 114:

/* Line 1806 of yacc.c  */
#line 2850 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 34 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 115:

/* Line 1806 of yacc.c  */
#line 2891 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 103 );

    }
    break;

  case 116:

/* Line 1806 of yacc.c  */
#line 2898 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 103 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 117:

/* Line 1806 of yacc.c  */
#line 2909 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 103 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 118:

/* Line 1806 of yacc.c  */
#line 2920 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 89 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 119:

/* Line 1806 of yacc.c  */
#line 2931 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 89 );

    }
    break;

  case 120:

/* Line 1806 of yacc.c  */
#line 2938 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 121:

/* Line 1806 of yacc.c  */
#line 2949 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 122:

/* Line 1806 of yacc.c  */
#line 2960 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 123:

/* Line 1806 of yacc.c  */
#line 2977 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 124:

/* Line 1806 of yacc.c  */
#line 3000 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 125:

/* Line 1806 of yacc.c  */
#line 3023 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 40 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 126:

/* Line 1806 of yacc.c  */
#line 3052 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 74 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 127:

/* Line 1806 of yacc.c  */
#line 3075 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 74 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 128:

/* Line 1806 of yacc.c  */
#line 3086 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 63 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 129:

/* Line 1806 of yacc.c  */
#line 3097 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 63 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 130:

/* Line 1806 of yacc.c  */
#line 3114 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 63 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 131:

/* Line 1806 of yacc.c  */
#line 3143 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 31 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 132:

/* Line 1806 of yacc.c  */
#line 3166 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 31 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 133:

/* Line 1806 of yacc.c  */
#line 3201 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 31 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 134:

/* Line 1806 of yacc.c  */
#line 3212 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 31 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 135:

/* Line 1806 of yacc.c  */
#line 3235 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 50 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 136:

/* Line 1806 of yacc.c  */
#line 3252 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 50 );

    }
    break;

  case 137:

/* Line 1806 of yacc.c  */
#line 3259 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 47 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 138:

/* Line 1806 of yacc.c  */
#line 3282 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 47 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 139:

/* Line 1806 of yacc.c  */
#line 3299 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 47 );

        (yyval.t)->addChild((yyvsp[(1) - (8)].t));

        (yyvsp[(1) - (8)].t)->parent= (yyval.t);

        (yyvsp[(1) - (8)].t)->nextSibbling= (yyvsp[(2) - (8)].t);

        (yyval.t)->addChild((yyvsp[(2) - (8)].t));

        (yyvsp[(2) - (8)].t)->parent= (yyval.t);

        (yyvsp[(2) - (8)].t)->nextSibbling= (yyvsp[(3) - (8)].t);

        (yyval.t)->addChild((yyvsp[(3) - (8)].t));

        (yyvsp[(3) - (8)].t)->parent= (yyval.t);

        (yyvsp[(3) - (8)].t)->nextSibbling= (yyvsp[(4) - (8)].t);

        (yyval.t)->addChild((yyvsp[(4) - (8)].t));

        (yyvsp[(4) - (8)].t)->parent= (yyval.t);

        (yyvsp[(4) - (8)].t)->nextSibbling= (yyvsp[(5) - (8)].t);

        (yyval.t)->addChild((yyvsp[(5) - (8)].t));

        (yyvsp[(5) - (8)].t)->parent= (yyval.t);

        (yyvsp[(5) - (8)].t)->nextSibbling= (yyvsp[(6) - (8)].t);

        (yyval.t)->addChild((yyvsp[(6) - (8)].t));

        (yyvsp[(6) - (8)].t)->parent= (yyval.t);

        (yyvsp[(6) - (8)].t)->nextSibbling= (yyvsp[(7) - (8)].t);

        (yyval.t)->addChild((yyvsp[(7) - (8)].t));

        (yyvsp[(7) - (8)].t)->parent= (yyval.t);

        (yyvsp[(7) - (8)].t)->nextSibbling= (yyvsp[(8) - (8)].t);

        (yyval.t)->addChild((yyvsp[(8) - (8)].t));

        (yyvsp[(8) - (8)].t)->parent= (yyval.t);

    }
    break;

  case 140:

/* Line 1806 of yacc.c  */
#line 3352 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 94 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 141:

/* Line 1806 of yacc.c  */
#line 3363 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 94 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 142:

/* Line 1806 of yacc.c  */
#line 3386 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 37 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 143:

/* Line 1806 of yacc.c  */
#line 3397 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 37 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 144:

/* Line 1806 of yacc.c  */
#line 3408 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 73 );

    }
    break;

  case 145:

/* Line 1806 of yacc.c  */
#line 3415 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 73 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 146:

/* Line 1806 of yacc.c  */
#line 3426 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 90 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 147:

/* Line 1806 of yacc.c  */
#line 3437 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 90 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 148:

/* Line 1806 of yacc.c  */
#line 3454 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 149:

/* Line 1806 of yacc.c  */
#line 3465 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 150:

/* Line 1806 of yacc.c  */
#line 3476 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 151:

/* Line 1806 of yacc.c  */
#line 3487 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 152:

/* Line 1806 of yacc.c  */
#line 3498 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 153:

/* Line 1806 of yacc.c  */
#line 3509 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 20 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 154:

/* Line 1806 of yacc.c  */
#line 3520 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 14 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 155:

/* Line 1806 of yacc.c  */
#line 3543 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 14 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 156:

/* Line 1806 of yacc.c  */
#line 3578 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 14 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 157:

/* Line 1806 of yacc.c  */
#line 3589 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 14 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 158:

/* Line 1806 of yacc.c  */
#line 3612 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 76 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 159:

/* Line 1806 of yacc.c  */
#line 3647 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 76 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 160:

/* Line 1806 of yacc.c  */
#line 3676 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 70 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 161:

/* Line 1806 of yacc.c  */
#line 3699 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 70 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 162:

/* Line 1806 of yacc.c  */
#line 3710 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 102 );

    }
    break;

  case 163:

/* Line 1806 of yacc.c  */
#line 3717 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 102 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 164:

/* Line 1806 of yacc.c  */
#line 3728 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 44 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 165:

/* Line 1806 of yacc.c  */
#line 3751 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 44 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 166:

/* Line 1806 of yacc.c  */
#line 3762 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 167:

/* Line 1806 of yacc.c  */
#line 3803 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 168:

/* Line 1806 of yacc.c  */
#line 3826 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 169:

/* Line 1806 of yacc.c  */
#line 3855 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 170:

/* Line 1806 of yacc.c  */
#line 3896 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 171:

/* Line 1806 of yacc.c  */
#line 3919 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 172:

/* Line 1806 of yacc.c  */
#line 3936 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 173:

/* Line 1806 of yacc.c  */
#line 3959 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 174:

/* Line 1806 of yacc.c  */
#line 3982 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 175:

/* Line 1806 of yacc.c  */
#line 4005 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 176:

/* Line 1806 of yacc.c  */
#line 4028 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 177:

/* Line 1806 of yacc.c  */
#line 4051 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 178:

/* Line 1806 of yacc.c  */
#line 4074 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 179:

/* Line 1806 of yacc.c  */
#line 4097 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 180:

/* Line 1806 of yacc.c  */
#line 4120 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 181:

/* Line 1806 of yacc.c  */
#line 4143 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 182:

/* Line 1806 of yacc.c  */
#line 4166 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 183:

/* Line 1806 of yacc.c  */
#line 4189 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 184:

/* Line 1806 of yacc.c  */
#line 4206 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 185:

/* Line 1806 of yacc.c  */
#line 4223 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 186:

/* Line 1806 of yacc.c  */
#line 4240 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 187:

/* Line 1806 of yacc.c  */
#line 4257 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 188:

/* Line 1806 of yacc.c  */
#line 4280 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 189:

/* Line 1806 of yacc.c  */
#line 4303 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 190:

/* Line 1806 of yacc.c  */
#line 4326 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 191:

/* Line 1806 of yacc.c  */
#line 4349 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 192:

/* Line 1806 of yacc.c  */
#line 4372 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 193:

/* Line 1806 of yacc.c  */
#line 4395 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 194:

/* Line 1806 of yacc.c  */
#line 4418 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 195:

/* Line 1806 of yacc.c  */
#line 4441 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 196:

/* Line 1806 of yacc.c  */
#line 4464 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 197:

/* Line 1806 of yacc.c  */
#line 4487 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 198:

/* Line 1806 of yacc.c  */
#line 4510 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 199:

/* Line 1806 of yacc.c  */
#line 4533 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 200:

/* Line 1806 of yacc.c  */
#line 4556 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 201:

/* Line 1806 of yacc.c  */
#line 4579 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 202:

/* Line 1806 of yacc.c  */
#line 4602 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 203:

/* Line 1806 of yacc.c  */
#line 4625 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 204:

/* Line 1806 of yacc.c  */
#line 4642 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 205:

/* Line 1806 of yacc.c  */
#line 4659 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 206:

/* Line 1806 of yacc.c  */
#line 4676 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 207:

/* Line 1806 of yacc.c  */
#line 4693 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 208:

/* Line 1806 of yacc.c  */
#line 4716 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 209:

/* Line 1806 of yacc.c  */
#line 4739 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 210:

/* Line 1806 of yacc.c  */
#line 4762 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 211:

/* Line 1806 of yacc.c  */
#line 4785 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 212:

/* Line 1806 of yacc.c  */
#line 4808 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 213:

/* Line 1806 of yacc.c  */
#line 4831 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 214:

/* Line 1806 of yacc.c  */
#line 4854 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 215:

/* Line 1806 of yacc.c  */
#line 4877 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 216:

/* Line 1806 of yacc.c  */
#line 4900 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 217:

/* Line 1806 of yacc.c  */
#line 4923 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 218:

/* Line 1806 of yacc.c  */
#line 4958 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 219:

/* Line 1806 of yacc.c  */
#line 4969 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 220:

/* Line 1806 of yacc.c  */
#line 4986 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 221:

/* Line 1806 of yacc.c  */
#line 5003 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 222:

/* Line 1806 of yacc.c  */
#line 5020 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 223:

/* Line 1806 of yacc.c  */
#line 5037 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 224:

/* Line 1806 of yacc.c  */
#line 5054 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 225:

/* Line 1806 of yacc.c  */
#line 5071 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 226:

/* Line 1806 of yacc.c  */
#line 5088 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 227:

/* Line 1806 of yacc.c  */
#line 5105 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 228:

/* Line 1806 of yacc.c  */
#line 5122 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 229:

/* Line 1806 of yacc.c  */
#line 5133 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 230:

/* Line 1806 of yacc.c  */
#line 5162 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 231:

/* Line 1806 of yacc.c  */
#line 5185 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 51 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 232:

/* Line 1806 of yacc.c  */
#line 5202 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 19 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 233:

/* Line 1806 of yacc.c  */
#line 5231 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 19 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 234:

/* Line 1806 of yacc.c  */
#line 5272 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 19 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 235:

/* Line 1806 of yacc.c  */
#line 5313 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 19 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 236:

/* Line 1806 of yacc.c  */
#line 5342 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 86 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 237:

/* Line 1806 of yacc.c  */
#line 5353 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 24 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 238:

/* Line 1806 of yacc.c  */
#line 5364 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 24 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 239:

/* Line 1806 of yacc.c  */
#line 5375 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 101 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 240:

/* Line 1806 of yacc.c  */
#line 5404 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 101 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 241:

/* Line 1806 of yacc.c  */
#line 5415 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 15 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 242:

/* Line 1806 of yacc.c  */
#line 5432 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 15 );

    }
    break;

  case 243:

/* Line 1806 of yacc.c  */
#line 5439 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 16 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 244:

/* Line 1806 of yacc.c  */
#line 5456 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 9 );

    }
    break;

  case 245:

/* Line 1806 of yacc.c  */
#line 5463 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 9 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 246:

/* Line 1806 of yacc.c  */
#line 5480 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 9 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 247:

/* Line 1806 of yacc.c  */
#line 5503 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 10 );

    }
    break;

  case 248:

/* Line 1806 of yacc.c  */
#line 5510 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 10 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 249:

/* Line 1806 of yacc.c  */
#line 5533 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 250:

/* Line 1806 of yacc.c  */
#line 5544 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 251:

/* Line 1806 of yacc.c  */
#line 5555 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 252:

/* Line 1806 of yacc.c  */
#line 5566 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 253:

/* Line 1806 of yacc.c  */
#line 5577 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 254:

/* Line 1806 of yacc.c  */
#line 5588 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 255:

/* Line 1806 of yacc.c  */
#line 5599 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 256:

/* Line 1806 of yacc.c  */
#line 5610 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 82 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 257:

/* Line 1806 of yacc.c  */
#line 5621 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 258:

/* Line 1806 of yacc.c  */
#line 5632 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 259:

/* Line 1806 of yacc.c  */
#line 5643 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 260:

/* Line 1806 of yacc.c  */
#line 5660 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 261:

/* Line 1806 of yacc.c  */
#line 5677 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 262:

/* Line 1806 of yacc.c  */
#line 5706 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 3 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 263:

/* Line 1806 of yacc.c  */
#line 5717 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 32 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 264:

/* Line 1806 of yacc.c  */
#line 5740 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 265:

/* Line 1806 of yacc.c  */
#line 5751 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 266:

/* Line 1806 of yacc.c  */
#line 5762 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 267:

/* Line 1806 of yacc.c  */
#line 5773 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 268:

/* Line 1806 of yacc.c  */
#line 5784 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 269:

/* Line 1806 of yacc.c  */
#line 5807 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 270:

/* Line 1806 of yacc.c  */
#line 5830 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 1 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 271:

/* Line 1806 of yacc.c  */
#line 5853 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 100 );

    }
    break;

  case 272:

/* Line 1806 of yacc.c  */
#line 5860 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 100 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 273:

/* Line 1806 of yacc.c  */
#line 5877 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 81 );

    }
    break;

  case 274:

/* Line 1806 of yacc.c  */
#line 5884 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 81 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 275:

/* Line 1806 of yacc.c  */
#line 5895 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 23 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 276:

/* Line 1806 of yacc.c  */
#line 5930 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 23 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 277:

/* Line 1806 of yacc.c  */
#line 5953 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 23 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 278:

/* Line 1806 of yacc.c  */
#line 5976 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 23 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 279:

/* Line 1806 of yacc.c  */
#line 5987 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 71 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 280:

/* Line 1806 of yacc.c  */
#line 5998 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 71 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 281:

/* Line 1806 of yacc.c  */
#line 6009 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 96 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 282:

/* Line 1806 of yacc.c  */
#line 6020 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 2 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 283:

/* Line 1806 of yacc.c  */
#line 6031 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 52 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 284:

/* Line 1806 of yacc.c  */
#line 6042 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 97 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 285:

/* Line 1806 of yacc.c  */
#line 6077 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 97 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 286:

/* Line 1806 of yacc.c  */
#line 6088 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 83 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 287:

/* Line 1806 of yacc.c  */
#line 6105 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 83 );

    }
    break;

  case 288:

/* Line 1806 of yacc.c  */
#line 6112 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 99 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 289:

/* Line 1806 of yacc.c  */
#line 6135 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 98 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 290:

/* Line 1806 of yacc.c  */
#line 6158 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 98 );

    }
    break;

  case 291:

/* Line 1806 of yacc.c  */
#line 6165 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 84 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 292:

/* Line 1806 of yacc.c  */
#line 6176 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 84 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 293:

/* Line 1806 of yacc.c  */
#line 6193 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 88 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 294:

/* Line 1806 of yacc.c  */
#line 6216 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 26 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 295:

/* Line 1806 of yacc.c  */
#line 6227 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 26 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 296:

/* Line 1806 of yacc.c  */
#line 6238 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 53 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 297:

/* Line 1806 of yacc.c  */
#line 6249 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 53 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 298:

/* Line 1806 of yacc.c  */
#line 6266 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 53 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 299:

/* Line 1806 of yacc.c  */
#line 6277 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 6 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 300:

/* Line 1806 of yacc.c  */
#line 6306 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 6 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 301:

/* Line 1806 of yacc.c  */
#line 6335 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 6 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 302:

/* Line 1806 of yacc.c  */
#line 6346 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 72 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 303:

/* Line 1806 of yacc.c  */
#line 6357 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 72 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 304:

/* Line 1806 of yacc.c  */
#line 6386 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 61 );

    }
    break;

  case 305:

/* Line 1806 of yacc.c  */
#line 6393 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 61 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 306:

/* Line 1806 of yacc.c  */
#line 6404 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 29 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 307:

/* Line 1806 of yacc.c  */
#line 6415 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 29 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 308:

/* Line 1806 of yacc.c  */
#line 6426 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 36 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 309:

/* Line 1806 of yacc.c  */
#line 6455 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 36 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 310:

/* Line 1806 of yacc.c  */
#line 6484 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 36 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 311:

/* Line 1806 of yacc.c  */
#line 6495 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 58 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 312:

/* Line 1806 of yacc.c  */
#line 6506 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 58 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 313:

/* Line 1806 of yacc.c  */
#line 6529 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 67 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 314:

/* Line 1806 of yacc.c  */
#line 6540 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 67 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 315:

/* Line 1806 of yacc.c  */
#line 6557 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 39 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 316:

/* Line 1806 of yacc.c  */
#line 6580 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 39 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 317:

/* Line 1806 of yacc.c  */
#line 6591 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 13 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 318:

/* Line 1806 of yacc.c  */
#line 6602 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 13 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 319:

/* Line 1806 of yacc.c  */
#line 6631 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 13 );

    }
    break;

  case 320:

/* Line 1806 of yacc.c  */
#line 6638 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 68 );

    }
    break;

  case 321:

/* Line 1806 of yacc.c  */
#line 6645 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 68 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 322:

/* Line 1806 of yacc.c  */
#line 6662 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (5)].t));

        (yyvsp[(1) - (5)].t)->parent= (yyval.t);

        (yyvsp[(1) - (5)].t)->nextSibbling= (yyvsp[(2) - (5)].t);

        (yyval.t)->addChild((yyvsp[(2) - (5)].t));

        (yyvsp[(2) - (5)].t)->parent= (yyval.t);

        (yyvsp[(2) - (5)].t)->nextSibbling= (yyvsp[(3) - (5)].t);

        (yyval.t)->addChild((yyvsp[(3) - (5)].t));

        (yyvsp[(3) - (5)].t)->parent= (yyval.t);

        (yyvsp[(3) - (5)].t)->nextSibbling= (yyvsp[(4) - (5)].t);

        (yyval.t)->addChild((yyvsp[(4) - (5)].t));

        (yyvsp[(4) - (5)].t)->parent= (yyval.t);

        (yyvsp[(4) - (5)].t)->nextSibbling= (yyvsp[(5) - (5)].t);

        (yyval.t)->addChild((yyvsp[(5) - (5)].t));

        (yyvsp[(5) - (5)].t)->parent= (yyval.t);

    }
    break;

  case 323:

/* Line 1806 of yacc.c  */
#line 6697 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 324:

/* Line 1806 of yacc.c  */
#line 6720 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 325:

/* Line 1806 of yacc.c  */
#line 6743 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 326:

/* Line 1806 of yacc.c  */
#line 6754 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 327:

/* Line 1806 of yacc.c  */
#line 6795 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 328:

/* Line 1806 of yacc.c  */
#line 6824 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 329:

/* Line 1806 of yacc.c  */
#line 6853 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 60 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 330:

/* Line 1806 of yacc.c  */
#line 6870 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 331:

/* Line 1806 of yacc.c  */
#line 6887 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 332:

/* Line 1806 of yacc.c  */
#line 6904 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 333:

/* Line 1806 of yacc.c  */
#line 6921 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 334:

/* Line 1806 of yacc.c  */
#line 6938 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 335:

/* Line 1806 of yacc.c  */
#line 6955 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 336:

/* Line 1806 of yacc.c  */
#line 6972 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 337:

/* Line 1806 of yacc.c  */
#line 6989 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 338:

/* Line 1806 of yacc.c  */
#line 7006 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 339:

/* Line 1806 of yacc.c  */
#line 7023 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 340:

/* Line 1806 of yacc.c  */
#line 7040 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 341:

/* Line 1806 of yacc.c  */
#line 7057 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 78 );

    }
    break;

  case 342:

/* Line 1806 of yacc.c  */
#line 7064 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 343:

/* Line 1806 of yacc.c  */
#line 7075 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 344:

/* Line 1806 of yacc.c  */
#line 7104 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 345:

/* Line 1806 of yacc.c  */
#line 7127 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 346:

/* Line 1806 of yacc.c  */
#line 7150 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (6)].t));

        (yyvsp[(1) - (6)].t)->parent= (yyval.t);

        (yyvsp[(1) - (6)].t)->nextSibbling= (yyvsp[(2) - (6)].t);

        (yyval.t)->addChild((yyvsp[(2) - (6)].t));

        (yyvsp[(2) - (6)].t)->parent= (yyval.t);

        (yyvsp[(2) - (6)].t)->nextSibbling= (yyvsp[(3) - (6)].t);

        (yyval.t)->addChild((yyvsp[(3) - (6)].t));

        (yyvsp[(3) - (6)].t)->parent= (yyval.t);

        (yyvsp[(3) - (6)].t)->nextSibbling= (yyvsp[(4) - (6)].t);

        (yyval.t)->addChild((yyvsp[(4) - (6)].t));

        (yyvsp[(4) - (6)].t)->parent= (yyval.t);

        (yyvsp[(4) - (6)].t)->nextSibbling= (yyvsp[(5) - (6)].t);

        (yyval.t)->addChild((yyvsp[(5) - (6)].t));

        (yyvsp[(5) - (6)].t)->parent= (yyval.t);

        (yyvsp[(5) - (6)].t)->nextSibbling= (yyvsp[(6) - (6)].t);

        (yyval.t)->addChild((yyvsp[(6) - (6)].t));

        (yyvsp[(6) - (6)].t)->parent= (yyval.t);

    }
    break;

  case 347:

/* Line 1806 of yacc.c  */
#line 7191 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 11 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 348:

/* Line 1806 of yacc.c  */
#line 7214 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 43 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 349:

/* Line 1806 of yacc.c  */
#line 7225 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 43 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 350:

/* Line 1806 of yacc.c  */
#line 7236 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 43 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 351:

/* Line 1806 of yacc.c  */
#line 7247 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 352:

/* Line 1806 of yacc.c  */
#line 7276 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 353:

/* Line 1806 of yacc.c  */
#line 7305 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 354:

/* Line 1806 of yacc.c  */
#line 7322 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 355:

/* Line 1806 of yacc.c  */
#line 7339 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (4)].t));

        (yyvsp[(1) - (4)].t)->parent= (yyval.t);

        (yyvsp[(1) - (4)].t)->nextSibbling= (yyvsp[(2) - (4)].t);

        (yyval.t)->addChild((yyvsp[(2) - (4)].t));

        (yyvsp[(2) - (4)].t)->parent= (yyval.t);

        (yyvsp[(2) - (4)].t)->nextSibbling= (yyvsp[(3) - (4)].t);

        (yyval.t)->addChild((yyvsp[(3) - (4)].t));

        (yyvsp[(3) - (4)].t)->parent= (yyval.t);

        (yyvsp[(3) - (4)].t)->nextSibbling= (yyvsp[(4) - (4)].t);

        (yyval.t)->addChild((yyvsp[(4) - (4)].t));

        (yyvsp[(4) - (4)].t)->parent= (yyval.t);

    }
    break;

  case 356:

/* Line 1806 of yacc.c  */
#line 7368 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 357:

/* Line 1806 of yacc.c  */
#line 7385 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 21 );

        (yyval.t)->addChild((yyvsp[(1) - (2)].t));

        (yyvsp[(1) - (2)].t)->parent= (yyval.t);

        (yyvsp[(1) - (2)].t)->nextSibbling= (yyvsp[(2) - (2)].t);

        (yyval.t)->addChild((yyvsp[(2) - (2)].t));

        (yyvsp[(2) - (2)].t)->parent= (yyval.t);

    }
    break;

  case 358:

/* Line 1806 of yacc.c  */
#line 7402 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 54 );

        (yyval.t)->addChild((yyvsp[(1) - (1)].t));

        (yyvsp[(1) - (1)].t)->parent= (yyval.t);

    }
    break;

  case 359:

/* Line 1806 of yacc.c  */
#line 7413 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 54 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;

  case 360:

/* Line 1806 of yacc.c  */
#line 7436 "pt_zend_language_parser.y"
    {
        (yyval.t)= new NonTerminal( 18 );

        (yyval.t)->addChild((yyvsp[(1) - (3)].t));

        (yyvsp[(1) - (3)].t)->parent= (yyval.t);

        (yyvsp[(1) - (3)].t)->nextSibbling= (yyvsp[(2) - (3)].t);

        (yyval.t)->addChild((yyvsp[(2) - (3)].t));

        (yyvsp[(2) - (3)].t)->parent= (yyval.t);

        (yyvsp[(2) - (3)].t)->nextSibbling= (yyvsp[(3) - (3)].t);

        (yyval.t)->addChild((yyvsp[(3) - (3)].t));

        (yyvsp[(3) - (3)].t)->parent= (yyval.t);

    }
    break;



/* Line 1806 of yacc.c  */
#line 11473 "pt_zend_language_parser.tab.cc"
      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYEMPTY : YYTRANSLATE (yychar);

  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
# define YYSYNTAX_ERROR yysyntax_error (&yymsg_alloc, &yymsg, \
                                        yyssp, yytoken)
      {
        char const *yymsgp = YY_("syntax error");
        int yysyntax_error_status;
        yysyntax_error_status = YYSYNTAX_ERROR;
        if (yysyntax_error_status == 0)
          yymsgp = yymsg;
        else if (yysyntax_error_status == 1)
          {
            if (yymsg != yymsgbuf)
              YYSTACK_FREE (yymsg);
            yymsg = (char *) YYSTACK_ALLOC (yymsg_alloc);
            if (!yymsg)
              {
                yymsg = yymsgbuf;
                yymsg_alloc = sizeof yymsgbuf;
                yysyntax_error_status = 2;
              }
            else
              {
                yysyntax_error_status = YYSYNTAX_ERROR;
                yymsgp = yymsg;
              }
          }
        yyerror (yymsgp);
        if (yysyntax_error_status == 2)
          goto yyexhaustedlab;
      }
# undef YYSYNTAX_ERROR
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;


      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  *++yyvsp = yylval;


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined(yyoverflow) || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}



/* Line 2067 of yacc.c  */
#line 7460 "pt_zend_language_parser.y"



#include <stdio.h>

extern char yytext[];
extern int column;
extern int line;

void yyerror( char *s)
{
fflush(stdout);
fprintf(stderr,"%s: %d.%d\n",s,line,column);
}



