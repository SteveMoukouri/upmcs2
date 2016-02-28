all: rapport

rapport: rapport.tex annexe
	pdflatex rapport.tex

annexe: annexe.md
	pandoc -o annexe.tex annexe.md

clean:
	rm *.log *.aux *~
