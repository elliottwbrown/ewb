# Edit bot.pd to contain the class name of your player
#
# port 55000 = 10-player humans & bots
# port 55001 = heads-up vs poki
# port 55002 - heads-up vs rutn

 java -cp jpoker.jar poker.online.BotPlayer \
bot.pd games.cs.ualberta.ca 55002 OpenPoki test
