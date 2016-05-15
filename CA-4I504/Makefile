TARGETS = td01.pdf td02.pdf td03.pdf td04.pdf td06.pdf td07.pdf

all: pdfs

pdfs: $(TARGETS)

%.pdf: %.md
	pandoc $< -o $@

clean:
	rm -rf *~
