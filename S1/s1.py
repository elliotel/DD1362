# coding: utf-8
########################################################################
# Mall för labb S1, DD1361 Programmeringsparadigm.
# Författare: Per Austrin
########################################################################

########################################################################
# Dessa funktioner är det som ska skrivas för att lösa de olika
# uppgifterna i labblydelsen.
########################################################################

def dna():          # uppgift 1
    return r"^[ACGT]+$"
    #return not "[^ACGT]"

def sorted():       # uppgift 2
    #return "^9*8*7*6*5*4*3*2*1*0*+$"
    return r"^9*8*7*6*5*4*3*2*1*0*$"

def hidden1(x):     # uppgift 3
    # indata x är strängen som vi vill konstruera ett regex för att söka efter
    return x

def hidden2(x):     # uppgift 4
    # indata x är strängen som vi vill konstruera ett regex för att söka efter
    return r".*".join(x)

def equation():     # uppgift 5
    #return "^-?\d+([-+*/]\d+)*$"
    #return "^[-+]?\d+([-+*/=]\d+)*$"
    return r"^[-+]?\d+([-+*/]\d+)*(=?[-+]?\d+([-+*/]\d+)*)?$"

def parentheses():  # uppgift 6
    #return "^([(]([(]([(]([(]([(][)])*[)])*[)])*[)])*[)])*$"
    return r"^(\((\((\((\((\(\))*\))*\))*\))*\))*$"


def sorted3():      # uppgift 7
    #return "^\d*(012|123|234|345|456|567|678|789)\d*$"
    return r"^\d*(01[2-9]|[0-1]2[3-9]|[0-2]3[4-9]|[0-3]4[5-9]|[0-4]5[6-9]|[0-5]6[7-9]|[0-6]7[8-9]|[0-7]89)\d*$"

def hidden2_med_lika_glapp(x, line):
    # indata x är strängen som vi vill konstruera ett regex för att söka efter
    regex = ""
    for i in range(len(line)):
        stringToInsert = ".{" + str(i) + "}"
        regex += "(" + stringToInsert.join(x)+ ")"
        if (i < len(line) - 1):
            regex += "|"
    return regex

def equation_with_parenthesis():
    return r"^[-+]?(\d+)|((\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*\))*\))*\))*\))*\)))(=?[-+]?(\d+)|((\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*(\([-+]?\d+([-+*/]\d+)*\))*\))*\))*\))*\))))?$"


########################################################################
# Raderna nedan är lite testkod som du kan använda för att provköra
# dina regexar.  Koden definierar en main-metod som läser rader från
# standard input och kollar vilka av de olika regexarna som matchar
# indata-raden.  För de två hidden-uppgifterna används söksträngen
# x="test" (kan lätt ändras nedan).  Du behöver inte sätta dig in i hur
# koden nedan fungerar om du inte känner för det.
#
# För att provköra från terminal, kör:
# $ python s1.py
# Skriv in teststrängar:
# [skriv något roligt]
# ...
########################################################################
from sys import stdin
import re

def main():
    print('Skriv in söksträng:')
    x = stdin.readline().rstrip('\r\n')
    def hidden1_test(): return hidden1(x)
    def hidden2_test(): return hidden2(x)
    tasks = [dna, sorted, hidden1_test, hidden2_test, equation, parentheses, sorted3]
    print('Skriv in teststrängar:')
    while True:
        line = stdin.readline().rstrip('\r\n')
        if line == '': break
        for task in tasks:
            result = '' if re.search(task(), line) else 'INTE '
            print('%s(): "%s" matchar %suttrycket "%s"' % (task.__name__, line, result, task()))
        
        result = '' if re.search(hidden2_med_lika_glapp(x, line), line) else 'INTE '
        print('%s(): "%s" matchar %suttrycket "%s"' % (hidden2_med_lika_glapp.__name__, line, result, hidden2_med_lika_glapp(x, line)))

        
        result = '' if re.search(equation_with_parenthesis(), line) else 'INTE '
        print('%s(): "%s" matchar %suttrycket "%s"' % (equation_with_parenthesis.__name__, line, result, equation_with_parenthesis()))


if __name__ == '__main__': main()