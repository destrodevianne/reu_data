# Made by Mr. Have fun! Version 0.2.1 cheked & fix by Ryo Saeba
# Shadow Weapon Coupons contributed by BiTi for the Official L2J Datapack Project
# Visit http://www.l2jdp.com/forum/ for more details
import sys
from l2r.gameserver.model.quest import State
from l2r.gameserver.model.quest import QuestState
from l2r.gameserver.model.quest.jython import QuestJython as JQuest
from l2r.gameserver.network.serverpackets      import SocialAction

qn = "409_PathToOracle"

CRYSTAL_MEDALLION = 1231
MONEY_OF_SWINDLER = 1232
DAIRY_OF_ALLANA = 1233
LIZARD_CAPTAIN_ORDER = 1234
LEAF_OF_ORACLE = 1235
HALF_OF_DAIRY = 1236
TAMATOS_NECKLACE = 1275

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [MONEY_OF_SWINDLER, DAIRY_OF_ALLANA, LIZARD_CAPTAIN_ORDER, CRYSTAL_MEDALLION, HALF_OF_DAIRY, TAMATOS_NECKLACE]

 def onEvent (self,event,st) :
    htmltext = event
    level = st.getPlayer().getLevel()
    classId = st.getPlayer().getClassId().getId()
    if event == "1" :
        st.set("id","0")
        if level >= 18 and classId == 0x19 and st.getQuestItemsCount(LEAF_OF_ORACLE) == 0 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
          st.giveItems(CRYSTAL_MEDALLION,1)
          htmltext = "30293-05.htm"
        elif classId != 0x19 :
            if classId == 0x1d :
              htmltext = "30293-02a.htm"
            else:
              htmltext = "30293-02.htm"
        elif level<18 and classId == 0x19 :
            htmltext = "30293-03.htm"
        elif level >= 18 and classId == 0x19 and st.getQuestItemsCount(LEAF_OF_ORACLE) == 1 :
            htmltext = "30293-04.htm"
    elif event == "30424-08.htm" :
        if st.getInt("cond") :
           st.addSpawn(27032)
           st.addSpawn(27033)
           st.addSpawn(27034)
           st.set("cond","2")
    elif event == "30424_1" :
        htmltext=""
    elif event == "30428_1" :
        if st.getInt("cond") :
           htmltext = "30428-02.htm"
    elif event == "30428_2" :
        if st.getInt("cond") :
           htmltext = "30428-03.htm"
    elif event == "30428_3" :
        if st.getInt("cond") :
           st.addSpawn(27035)
    return htmltext


 def onTalk (self,npc,player):
   htmltext = Quest.getNoQuestMsg(player)
   st = player.getQuestState(qn)
   if not st : return htmltext

   npcId = npc.getNpcId()
   id = st.getState()
   if npcId != 30293 and id != State.STARTED : return htmltext

   if npcId == 30293 and st.getInt("cond")==0 :
      if st.getQuestItemsCount(LEAF_OF_ORACLE) == 0 :
         htmltext = "30293-01.htm"
         return htmltext
      else:
         htmltext = "30293-04.htm"
   elif npcId == 30293 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION) :
    if st.getQuestItemsCount(MONEY_OF_SWINDLER) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 0 and st.getQuestItemsCount(HALF_OF_DAIRY) == 0 :
        if st.getInt("cond") :
            htmltext = "30293-09.htm"
        else:
            htmltext = "30293-06.htm"
    else:
          if st.getQuestItemsCount(MONEY_OF_SWINDLER) == 1 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 1 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY) == 0 :
            htmltext = "30293-08.htm"
            st.takeItems(MONEY_OF_SWINDLER,1)
            st.takeItems(DAIRY_OF_ALLANA,1)
            st.takeItems(LIZARD_CAPTAIN_ORDER,1)
            st.takeItems(CRYSTAL_MEDALLION,1)
            st.giveItems(LEAF_OF_ORACLE,1)
            isFinished = st.getGlobalQuestVar("1ClassQuestFinished")
            if isFinished == "" : 
              if player.getLevel() >= 20 :
                st.addExpAndSp(320534, 20392)
              elif player.getLevel() == 19 :
                st.addExpAndSp(456128, 27090)
              else:
                st.addExpAndSp(591724, 33788)
              st.giveItems(57, 163800)
            st.set("cond","0")
            st.exitQuest(False)
            st.saveGlobalQuestVar("1ClassQuestFinished","1")
            st.playSound("ItemSound.quest_finish")
            player.sendPacket(SocialAction(player.getObjectId(),3))
          else:
            htmltext = "30293-07.htm"
   elif npcId == 30424 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION) :
        if st.getQuestItemsCount(MONEY_OF_SWINDLER) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 0 and st.getQuestItemsCount(HALF_OF_DAIRY) == 0 :
          if st.getInt("cond") > 2:
            htmltext = "30424-05.htm"
          else:
            htmltext = "30424-01.htm"
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY) == 0 :
            htmltext = "30424-02.htm"
            st.giveItems(HALF_OF_DAIRY,1)
            st.set("cond","4")
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY) == 1 :
              if st.getInt("cond") and st.getQuestItemsCount(TAMATOS_NECKLACE) == 0 :
                htmltext = "30424-06.htm"
              else:
                htmltext = "30424-03.htm"
        elif st.getQuestItemsCount(MONEY_OF_SWINDLER) == 1 and st.getQuestItemsCount(DAIRY_OF_ALLANA) == 0 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY) == 1 :
                htmltext = "30424-04.htm"
                st.takeItems(HALF_OF_DAIRY,1)
                st.giveItems(DAIRY_OF_ALLANA,1)
                st.set("cond","7")
        else:
                if st.getQuestItemsCount(MONEY_OF_SWINDLER) == 1 and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 1 and st.getQuestItemsCount(HALF_OF_DAIRY) == 0 and st.getQuestItemsCount(DAIRY_OF_ALLANA) :
                  htmltext = "30424-05.htm"
   elif npcId == 30428 and st.getInt("cond") and st.getQuestItemsCount(CRYSTAL_MEDALLION) and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) :
        if st.getQuestItemsCount(TAMATOS_NECKLACE) == 1 :
          st.giveItems(MONEY_OF_SWINDLER,1)
          st.takeItems(TAMATOS_NECKLACE,1)
          st.set("cond","6")
          htmltext = "30428-04.htm"
        else:
          if st.getQuestItemsCount(MONEY_OF_SWINDLER)>0 :
            htmltext = "30428-05.htm"
          else:
            if st.getInt("cond") > 4 :
              htmltext = "30428-06.htm"
            else:
              htmltext = "30428-01.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   st = player.getQuestState(qn)
   if not st : return 
   if st.getState() != State.STARTED : return 
   
   npcId = npc.getNpcId()
   if npcId == 27032 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LIZARD_CAPTAIN_ORDER) == 0 :
          st.giveItems(LIZARD_CAPTAIN_ORDER,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","3")
   elif npcId == 27035 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(TAMATOS_NECKLACE) == 0 :
          st.giveItems(TAMATOS_NECKLACE,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","5")
   return

QUEST       = Quest(409,qn,"Path To Oracle")

QUEST.addStartNpc(30293)

QUEST.addTalkId(30293)

QUEST.addTalkId(30424)
QUEST.addTalkId(30428)

QUEST.addKillId(27032)
QUEST.addKillId(27033)
QUEST.addKillId(27034)
QUEST.addKillId(27035)