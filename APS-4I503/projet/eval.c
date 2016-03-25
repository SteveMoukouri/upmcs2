#include <stdio.h>
#include <stdlib.h>
#include "prolog_gen.h"
#include "types.h"
#include "ast.h"
#include "eval.h"

#define TYPEFILE "tmptype.str"
#define TYPEPATH "typechecker.pl"

#ifdef DEBUG_EV
#define debug_ev(str) { fprintf(stderr, "%s\n", str); }
#else
#define debug_ev(str) ;
#endif

void eval_prog(AST* prog, Env ** env) {
  debug_ev("Prog");
  eval_cmds(prog->content.asCmds);
}

void eval_cmds(AST* cmds, Env ** env) {
  debug_ev("Cmds");
  eval_seq(cmds->content.asCmds, env);
}

void eval_seq(Cmds* cmds, Env ** env) {
  debug_ev("Seq");
  Env ** myEnv = env;
  while (cmds) {
    AST* statdec = cmds->statDec;
    switch (statDec->type) {
    case T_STAT:
      myEnv = eval_stat(statDec, myEnv);
      break;
    case T_DEC:
      myEnv = eval_dec(statDec, myEnv);
      break;
    }
    if (!cmds->next)
      break;
    cmds = cmds->next.content.asCmds;
  }
}

void eval_dec(AST* dec, Env ** env) {
  debug_ev("Dec");
  Dec * decl = dec->content.asDec;
  Env ** myEnv = env;
  Env * newValue = malloc(sizeof(*newValue));
  switch (decl->type) {
  case T_CONST:
    newValue->varType = EVAL_CNST;
    switch (newValue->varType) {
    case EVAL_INT:
      break;
    case EVAL_BOOL:
      break;
    }
    break;
  case T_VAR:
    newValue->varType = EVAL_VAR;
    switch (newValue->varType) {
    case EVAL_INT:
      int i = eval_expr(decl->expr);
      newValue->contents.asInt = i;
      break;
    case EVAL_BOOL:
      boolean b = eval_expr(
      break;
    }
    break;
  default:
    fprintf(stderr, "Unknown declaration type!\n");
    exit(EXIT_FAILURE);
  }
}

void eval_stat(AST* st, Env ** env) {
  Stat * stat = st->content.asStat;
  switch (stat->type) {
  case T_SET:    
    break;
  case T_IF:
    break;
  case T_WHILE:
    break;
  case T_ECHO:
    break;
  default:
    fprintf(stderr, "Unknown statement type!\n");
    exit(EXIT_FAILURE);
  }
}

void eval_type(AST* expr, Env ** env) {
  return NULL
}

int main(int argc, char** argv) {
  if (argc < 2) {
    fprintf(stderr, "No file specified\n");
    exit(EXIT_FAILURE);
  }
  else {
    AST* programAST;
    // Parse it!
    programAST = yyparse();
    // Typecheck it!
    FILE* typefp;
    if (!(typefp = fopen(TYPEFILE, "rw"))) {
      fprintf(stderr, "Unable to obtain temp file for type checking\n");
      exit(EXIT_FAILURE);
    }
    else {
      // Replace with pipe(), 
      FILE* swipl;
      print_prog(typefp, programAST);
      fflush(typefp);
      if (!(swipl = popen('/usr/bin/swipl', 'rw'))) {
	fprintf(stderr, "Unable to obtain prolog interpreter\n");
	exit(EXIT_FAILURE);
      }
      fprintf(swipl, "[%s].\n", TYPEPATH);
    }
    // Evaluate it!
  }
}
