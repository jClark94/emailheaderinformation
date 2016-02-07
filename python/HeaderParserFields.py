'''
Created on 20 Jan 2016

@author: joshua
'''

from email.parser import Parser
import re
import sys

header = ""
for arg in sys.argv:
    header = header + arg
headers = Parser().parsestr(header, True)

for (k,v) in headers.items() :
    if (k <> 'Received') :
        print k
        print re.sub(r"\n\t? *", ' ', v, 0)