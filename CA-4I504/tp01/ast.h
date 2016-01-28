#ifndef __ast_h__
#define __ast_h_

struct _ast_node;

typedef enum {APPLICATION, LAMBDADEF, IDENTIFIER} node_type;

typedef struct _appl 
{
  struct _ast_node * expr1;
  struct _ast_node * expr2;
} ast_node_appl;

typedef struct _ldef 
{
  struct _ast_node * idtf;
  struct _ast_node * expr;
} ast_node_ldef;

typedef struct _idtf {
  char * idtf;
} ast_node_idtf;

typedef struct _ast_node {
  node_type type;
  union _node {
    ast_node_appl * asAppl;
    ast_node_ldef * asLdef;
    ast_node_idtf * asIdtf;
  } node;
} ast_node;

ast_node * newAppl(ast_node * expr1, ast_node * expr2);
ast_node * newLdef(ast_node * expr, ast_node * idtf);
ast_node * newIdtf(char * idtf);
int countApplications(ast_node * root);

#endif
