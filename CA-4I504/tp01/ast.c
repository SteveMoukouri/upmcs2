#include "ast.h"
#include <stdlib.h>


ast_node * newAppl(ast_node * expr1, ast_node * expr2) {
  ast_node * ret = (ast_node *) malloc(sizeof(ast_node));
  ast_node_appl * appl = (ast_node_appl *) malloc(sizeof(ast_node_appl));
  appl->expr1 = expr1;
  appl->expr2 = expr2;
  ret->type = APPLICATION;
  ret->node.asAppl = appl;
  return ret;
}

ast_node * newLdef(ast_node * expr, ast_node * idtf) {
  ast_node * ret = (ast_node *) malloc(sizeof(ast_node));
  ast_node_ldef * ldef = (ast_node_ldef *) malloc(sizeof(ast_node_ldef));
  ldef->expr = expr;
  ldef->idtf = idtf;
  ret->type = LAMBDADEF;
  ret->node.asLdef = ldef;
  return ret;
} 

ast_node * newIdtf(char * idtf) {
  ast_node * ret = (ast_node *) malloc(sizeof(ast_node));
  ast_node_idtf * node = (ast_node_idtf *) malloc(sizeof(ast_node_idtf));
  node->idtf = idtf;
  ret->node.asIdtf = node;
  return ret;
}

int countApplications(ast_node * root) {
  switch (root->type) {
  case APPLICATION: return 1 + countApplications(root->node.asAppl->expr1) 
      + countApplications(root->node.asAppl->expr2);
  case LAMBDADEF: return countApplications(root->node.asLdef->expr);
  case IDENTIFIER: return 0;
  default: return 0;
  }
}
