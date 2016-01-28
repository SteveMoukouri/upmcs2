{}

rule program =
  parse ['+''-''*''/''%'] { print_string "Opérateur "; program lexbuf}
    | ['0'-'9']+ { print_string "Entier "; program lexbuf}
    | ';' { print_string "Séparateur "; program lexbuf}
    | ['\n''\t'' '] { program lexbuf }
    | eof {}

{
  program (Lexing.from_string "1 + 43; 1");
  print_string "\n";
}
