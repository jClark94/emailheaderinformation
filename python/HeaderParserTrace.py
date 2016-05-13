'''
Created on 20 Jan 2016

@author: joshua
'''

from email.parser import Parser
import re
import sys

header = ""
firstArg = True

'''
    Use the flag to miss out the first argument to make sure that the first line of the received
    fields is parsed properly
'''

for arg in sys.argv:
    if not(firstArg) :
        header = header + arg
    else :
        firstArg = False
headers = Parser().parsestr(header, True)
    
for output in headers.get_all('Received') :
    print 'Received: ' + re.sub(r"\s+",  ' ', output, 0)