#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "ast.h"


AST* make_prog (AST* cmds) {
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_PROG;
  ast->content.asProg->cmds = cmds;
  return ast;
}

AST* make_cmds (AST* statDec, AST* next) {
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_CMDS;
  ast->content.asCmds = malloc(sizeof(Cmds));
  ast->content.asCmds->statDec = statDec;
  ast->content.asCmds->next = next;
  return ast;
}

AST* make_dec (TypeDec ast_type, AST* dec_type, char* ident, AST* expr) {
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_DEC;
  ast->content.asDec = malloc(sizeof(Dec));
  ast->content.asDec->type = ast_type;
  ast->content.asDec->decType = dec_type;
  ast->content.asDec->ident = ident;
  ast->content.asDec->expr = expr;
  return ast;
}

AST* make_stat (TypeStat statType, char* ident, AST* expr, AST* prog1, AST* prog2) {
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_STAT;
  ast->content.asStat = malloc(sizeof(Stat));
  ast->content.asStat->type = statType;
  ast->content.asStat->ident = ident;
  ast->content.asStat->expr = expr;
  ast->content.asStat->prog1 = prog1;
  ast->content.asStat->prog2 = prog2;
  return ast;
}

AST* make_expr (TypeExpr exprType, Bool bool, int num, char* ident, Operators op, AST* expr1, AST* expr2) {
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_EXPR;
  ast->content.asExpr = malloc(sizeof(Expr));
  ast->content.asExpr->type = exprType;
  switch (exprType) {
  case T_E_BOOL:
    ast->content.asExpr->contents.bool = bool;
    break;
  case T_NUM:
    ast->content.asExpr->contents.num = num;
    break;
  case T_IDENT:
    ast->content.asExpr->contents.ident = ident;
    break;
  case T_UNOP:
    ast->content.asExpr->contents.unop->op = op;
    ast->content.asExpr->contents.unop->arg = expr1;
  case T_BINOP:
    ast->content.asExpr->contents.binop->op = op;
    ast->content.asExpr->contents.binop->arg1 = expr1;
    ast->content.asExpr->contents.binop->arg2 = expr2;
    break;
  }
  return ast;
}

AST* make_type (PrimitiveType t) {
  Type res = malloc(sizeof(*res));
  *res = t;
  AST* ast = malloc(sizeof(*ast));
  ast->type = T_TYPE;
  ast->content.asType = res;
  return ast;
}
