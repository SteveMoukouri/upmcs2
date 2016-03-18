#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "types.h"
#include "ast.h"
#include "prolog_gen.h"

char* concat2(char* str1, char* str2) {
  char *result = malloc(strlen(str1) + strlen(str2) + 1);
  strcpy(result, str1);
  strcat(result, str2);
  return result;
}

char* concat3(char* str1, char* sep, char* str2) {
  char *result = malloc(strlen(str1) + strlen(sep) + strlen(str2)+1);
  strcpy(result, str1);
  strcat(result, sep);
  strcat(result, str2);
  return result;
}

char* concat5(char* str1, char* str2, char* str3, char* str4, char* str5) {
  char *result = malloc(strlen(str1) + strlen(str2) + strlen(str3)
			+ strlen(str4) + strlen(str5)+1);
  strcpy(result, str1);
  strcat(result, str2);
  strcat(result, str3);
  strcat(result, str4);
  strcat(result, str5);
  return result;
}

char* concat7(char* str1, char* str2, char* str3, char* str4, char* str5, char* str6, char* str7) {
  char *result = malloc(strlen(str1) + strlen(str2) + strlen(str3)
			+ strlen(str4) + strlen(str5) + strlen(str6)
			+ strlen(str7)+1);
  strcpy(result, str1);
  strcat(result, str2);
  strcat(result, str3);
  strcat(result, str4);
  strcat(result, str5);
  strcat(result, str6);
  strcat(result, str7);
  return result;
}


char* application_unary(char* app, char* arg) {
  char *result = malloc(3 + 1 + strlen(app) + 1 + 1 + strlen(arg) + 1 + 1 + 1);
  //                    app (    fonction     ,   [    argument     ]   )   \0
  strcpy(result, "app(");
  strcat(result, app);
  strcat(result, ",[");
  strcat(result, arg);
  strcat(result, "])");
  return result;
}

char* application_binary(char* app, char* arg1, char* arg2) {
  char *result = malloc(4 + strlen(app) + 2 + strlen(arg1) + 1 + strlen(arg2) + 3);
  //                    app(  fonction  ,[    argument 1     ,   argument 2   ])\0
  strcpy(result, "app(");
  strcat(result, app);
  strcat(result, ",[");
  strcat(result, arg1);
  strcat(result, ",");
  strcat(result, arg2);
  strcat(result, "])");
  return result;
}

char* operator_binary(char* op, char* arg1, char* arg2) {
  char *result = malloc(strlen(op) + 1 + strlen(arg1) + 1 + strlen(arg2) + 2);
  //                    operator     (    argument 1     ,   argument 2   )\0
  strcpy(result, op);
  strcat(result, "(");
  strcat(result, arg1);
  strcat(result, ",");
  strcat(result, arg2);
  strcat(result, ")");
  return result;
}


char* itoa(int a) {
  int length = a / 10 + 1;
  char *result = malloc(length * sizeof(char));
  sprintf(result, "%d", a);
  return result;
}

char* make_prolog(AST* ast) {
  if (ast) {
    switch (ast->type) {
    case T_PROG:
      concat3("prog([", make_prolog(ast->asProg), "])");
      break;
    case T_CMDS:
      concat3("[", , "]");
      break;
    case T_DEC:
      switch (ast->asDec.type) {
      case T_CONST:
	
	break;
      case T_VAR:
	break;
      default:
	fprintf(stderr, "Unknown declaration type!\n");
	exit(EXIT_FAILURE);
      }
      break;
    case T_STAT:
      switch (ast->asStat.type) {
      case T_SET:
	break;
      case T_IF:
	break;
      case T_WHILE:
	break;
      default:
	fprintf(stderr, "Unknown statement type!\n");
	exit(EXIT_FAILURE);
      }
      break;
    case T_EXPR:
      Expr* expr = ast->asExpr;
      switch (expr.type) {
      case T_E_BOOL:
	break;
      case T_NUM:
	break;
      case T_IDENT:
	break;
      case T_UNOP:
	break;
      case T_BINOP:
	Binop* b = expr.contents->binop;
	switch (b->op) {
	case T_NOT:
	  break;
	case T_AND:
	  break;
	case T_OR:
	  break;
	case T_EQ:
	  break;
	case T_LT:
	  break;
	case T_ADD:
	  break;
	case T_SUB:
	  break;
	case T_MUL:
	  break;
	case T_DIV:
	  break;
	case T_MOD
	  break;
	default:
	  fprintf(stderr, "Unknown operand!\n");
	  exit(EXIT_FAILURE);
	}
	break;
      default:
	fprintf(stderr, "Unknown expression type!\n");
	exit(EXIT_FAILURE);
      }
      break;
    case T_TYPE:
      switch (*(ast->asType)) {
      case T_VOID:
	break;
      case T_BOOL:
	break;
      case T_INT:
	break;
      default:
	fprintf(stderr, "Unknown type!\n");
	exit(EXIT_FAILURE);
      }
      break;
    default:
      fprintf(stderr, "Unknown AST node type!\n");
      exit(EXIT_FAILURE);
    }
  } 
}
