#ifndef __AST__
#define __AST__
#include "types.h"

AST* make_prog (AST* cmds);
AST* make_cmds (AST* statDec, AST* next);
AST* make_dec (TypeDec ast_type, AST* dec_type, char* ident, AST* expr);
AST* make_stat (TypeStat statType, char* ident, AST* expr, AST* prog1, AST* prog2);
AST* make_expr (TypeExpr exprType, Bool bool, int num, char* ident, Operators op, AST* expr1, AST* expr2);
AST* make_type (PrimitiveType t);
AST* make_bool_expr (Bool b);
AST* make_integer_expr (int n);
AST* make_ident_expr (char* ident);
AST* make_unary_expr (Operators op, AST* expr);
AST* make_binary_expr (Operators op, AST* expr1, AST* expr2);

#endif
