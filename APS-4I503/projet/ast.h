#ifndef __AST__
#define __AST__
#include "types.h"

AST* make_prog (AST* cmds);
AST* make_cmds (AST* statDec, AST* next);
AST* make_var (char* ident, AST* type);
AST* make_cst(char* ident, AST* type, AST* expr);
AST* make_set(char* ident, AST* expr);
AST* make_cond(AST* cond, AST* cons, AST* alt);
AST* make_loop(AST* cond, AST* body);
AST* make_type (PrimitiveType t);
AST* make_bool_expr (Bool b);
AST* make_integer_expr (int n);
AST* make_ident_expr (char* ident);
AST* make_unary_expr (Operators op, AST* expr);
AST* make_binary_expr (Operators op, AST* expr1, AST* expr2);

#endif
