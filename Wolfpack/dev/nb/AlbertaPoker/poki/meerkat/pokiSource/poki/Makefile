DIR = classes/

SOURCES = poker/*.java \
          poker/ai/*.java \
          poker/ai/model/*.java \
          poker/ai/player/*.java \
          poker/online/*.java \

######################################
# jpoker.jar 
######################################
jpoker.jar: poki
	jar -cf jpoker.jar -C $(DIR) .

poki:
	javac -d $(DIR) $(SOURCES)

%.class : %.java
	javac -d ($DIR) $<

