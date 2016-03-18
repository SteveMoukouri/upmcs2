#ifndef __TYPES__H_
#define __TYPES__H_

typedef enum _typeNode  { T_PROG, T_CMDS, T_DEC, T_STAT, T_EXPR, T_TYPE } TypeNode;
typedef enum _typeDec   { T_CONST, T_VAR } TypeDec;
typedef enum _typeStat  { T_SET, T_IF, T_WHILE } TypeStat;
typedef enum _typeExpr  { T_E_BOOL, T_NUM, T_IDENT, T_UNOP, T_BINOP } TypeExpr;
typedef enum _operators { T_NOT, T_AND, T_OR, T_EQ, T_LT, T_ADD, T_SUB, T_MUL, T_DIV, T_MOD } Operators;
typedef enum _types     { T_VOID, T_BOOL, T_INT } PrimitiveType;
typedef enum _bool      { T_TRUE, T_FALSE } Bool;

struct _prog;

typedef struct _ast AST;

typedef PrimitiveType* Type;

typedef struct _unop {
  Operators op;
  AST * arg;
} UnOp;

typedef struct _binop {
  Operators op;
  AST * arg1;
  AST * arg2;
} BinOp;

typedef struct _expr {
  TypeExpr type;
  union {
    UnOp * unop;
    BinOp * binop;
    Bool bool;
    int num;
    char* ident;
  } contents;
} Expr;

typedef struct _stat {
  TypeStat type;
  char* ident; // NULL unless it's a SET
  AST* expr;
  AST* prog1; // NULL if it's a SET
  AST* prog2; // NULL if it's a SET or a WHILE
} Stat;

typedef struct _dec {
  TypeDec type;
  char* ident;
  AST* decType;
  AST* expr; // NULL if it's a VAR
} Dec;

typedef struct _cmds {
  AST* statDec; 
  AST* next; // Can be NULL.
} Cmds;

typedef struct _prog {
  AST* cmds;
} Prog;

/* ------------------------------------------ *\
                  MAIN STRUCT
\* ------------------------------------------ */
typedef struct _ast {
  TypeNode type;
  union {
    Prog* asProg;
    Cmds* asCmds;
    Dec* asDec;
    Stat* asStat;
    Expr* asExpr;
    Type asType;
    } content;
} AST;
    
#endif
