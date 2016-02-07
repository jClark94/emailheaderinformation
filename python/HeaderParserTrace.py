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
    
for output in headers.get_all('Received') :
    print 'Received: ' + re.sub(r"\n\t? *",  ' ', output, 0)