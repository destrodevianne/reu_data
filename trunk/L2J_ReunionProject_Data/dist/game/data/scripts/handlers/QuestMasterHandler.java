/*
 * Copyright (C) 2004-2014 L2J DataPack
 *
 * This file is part of L2J DataPack.
 *
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import l2r.gameserver.scripts.quests.*;

/**
 * @author Nos
 */
public class QuestMasterHandler
{
	private static final Logger _log = Logger.getLogger(QuestMasterHandler.class.getName());
	
	private static final Class<?>[] QUESTS =
	{
		Q00001_LettersOfLove.class,
		Q00002_WhatWomenWant.class,
		Q00003_WillTheSealBeBroken.class,
		Q00004_LongLiveThePaagrioLord.class,
		Q00005_MinersFavor.class,
		Q00006_StepIntoTheFuture.class,
		Q00007_ATripBegins.class,
		Q00008_AnAdventureBegins.class,
		Q00009_IntoTheCityOfHumans.class,
		Q00010_IntoTheWorld.class,
		Q00011_SecretMeetingWithKetraOrcs.class,
		Q00012_SecretMeetingWithVarkaSilenos.class,
		Q00013_ParcelDelivery.class,
		Q00014_WhereaboutsOfTheArchaeologist.class,
		Q00015_SweetWhispers.class,
		Q00016_TheComingDarkness.class,
		Q00017_LightAndDarkness.class,
		Q00018_MeetingWithTheGoldenRam.class,
		Q00019_GoToThePastureland.class,
		Q00020_BringUpWithLove.class,
		Q00021_HiddenTruth.class,
		Q00024_InhabitantsOfTheForestOfTheDead.class,
		Q00026_TiredOfWaiting.class,
		Q00027_ChestCaughtWithABaitOfWind.class,
		Q00028_ChestCaughtWithABaitOfIcyAir.class,
		Q00029_ChestCaughtWithABaitOfEarth.class,
		Q00030_ChestCaughtWithABaitOfFire.class,
		Q00031_SecretBuriedInTheSwamp.class,
		Q00032_AnObviousLie.class,
		Q00033_MakeAPairOfDressShoes.class,
		Q00034_InSearchOfCloth.class,
		Q00035_FindGlitteringJewelry.class,
		Q00036_MakeASewingKit.class,
		Q00037_MakeFormalWear.class,
		Q00038_DragonFangs.class,
		Q00039_RedEyedInvaders.class,
		Q00040_ASpecialOrder.class,
		Q00042_HelpTheUncle.class,
		Q00043_HelpTheSister.class,
		Q00044_HelpTheSon.class,
		Q00045_ToTalkingIsland.class,
		Q00046_OnceMoreInTheArmsOfTheMotherTree.class,
		Q00047_IntoTheDarkElvenForest.class,
		Q00048_ToTheImmortalPlateau.class,
		Q00049_TheRoadHome.class,
		Q00050_LanoscosSpecialBait.class,
		Q00051_OFullesSpecialBait.class,
		Q00052_WilliesSpecialBait.class,
		Q00053_LinnaeusSpecialBait.class,
		Q00062_PathOfTheTrooper.class,
		Q00063_PathOfTheWarder.class,
		Q00101_SwordOfSolidarity.class,
		Q00102_SeaOfSporesFever.class,
		Q00103_SpiritOfCraftsman.class,
		Q00104_SpiritOfMirrors.class,
		Q00105_SkirmishWithOrcs.class,
		Q00106_ForgottenTruth.class,
		Q00107_MercilessPunishment.class,
		Q00108_JumbleTumbleDiamondFuss.class,
		Q00109_InSearchOfTheNest.class,
		Q00110_ToThePrimevalIsle.class,
		Q00111_ElrokianHuntersProof.class,
		Q00112_WalkOfFate.class,
		Q00113_StatusOfTheBeaconTower.class,
		Q00114_ResurrectionOfAnOldManager.class,
		Q00115_TheOtherSideOfTruth.class,
		Q00116_BeyondTheHillsOfWinter.class,
		Q00119_LastImperialPrince.class,
		Q00121_PavelTheGiant.class,
		Q00122_OminousNews.class,
		Q00124_MeetingTheElroki.class,
		Q00125_TheNameOfEvil1.class,
		Q00126_TheNameOfEvil2.class,
		Q00128_PailakaSongOfIceAndFire.class,
		Q00129_PailakaDevilsLegacy.class,
		Q00130_PathToHellbound.class,
		Q00131_BirdInACage.class,
		Q00132_MatrasCuriosity.class,
		Q00133_ThatsBloodyHot.class,
		Q00134_TempleMissionary.class,
		Q00135_TempleExecutor.class,
		Q00136_MoreThanMeetsTheEye.class,
		Q00137_TempleChampionPart1.class,
		Q00138_TempleChampionPart2.class,
		Q00139_ShadowFoxPart1.class,
		Q00140_ShadowFoxPart2.class,
		Q00141_ShadowFoxPart3.class,
		Q00142_FallenAngelRequestOfDawn.class,
		Q00143_FallenAngelRequestOfDusk.class,
		Q00146_TheZeroHour.class,
		Q00147_PathtoBecominganEliteMercenary.class,
		Q00148_PathtoBecominganExaltedMercenary.class,
		Q00151_CureForFever.class,
		Q00152_ShardsOfGolem.class,
		Q00153_DeliverGoods.class,
		Q00154_SacrificeToTheSea.class,
		Q00155_FindSirWindawood.class,
		Q00156_MillenniumLove.class,
		Q00157_RecoverSmuggledGoods.class,
		Q00158_SeedOfEvil.class,
		Q00159_ProtectTheWaterSource.class,
		Q00160_NerupasRequest.class,
		Q00161_FruitOfTheMotherTree.class,
		Q00162_CurseOfTheUndergroundFortress.class,
		Q00163_LegacyOfThePoet.class,
		Q00164_BloodFiend.class,
		Q00165_ShilensHunt.class,
		Q00166_MassOfDarkness.class,
		Q00167_DwarvenKinship.class,
		Q00168_DeliverSupplies.class,
		Q00169_OffspringOfNightmares.class,
		Q00170_DangerousSeduction.class,
		Q00172_NewHorizons.class,
		Q00173_ToTheIsleOfSouls.class,
		Q00174_SupplyCheck.class,
		Q00175_TheWayOfTheWarrior.class,
		Q00176_StepsForHonor.class,
		Q00179_IntoTheLargeCavern.class,
		Q00182_NewRecruits.class,
		Q00183_RelicExploration.class,
		Q00186_ContractExecution.class,
		Q00187_NikolasHeart.class,
		Q00188_SealRemoval.class,
		Q00189_ContractCompletion.class,
		Q00190_LostDream.class,
		Q00191_VainConclusion.class,
		Q00192_SevenSignsSeriesOfDoubt.class,
		Q00193_SevenSignsDyingMessage.class,
		Q00194_SevenSignsMammonsContract.class,
		Q00195_SevenSignsSecretRitualOfThePriests.class,
		Q00196_SevenSignsSealOfTheEmperor.class,
		Q00197_SevenSignsTheSacredBookOfSeal.class,
		Q00198_SevenSignsEmbryo.class,
		Q00211_TrialOfTheChallenger.class,
		Q00212_TrialOfDuty.class,
		Q00230_TestOfSummoner.class,
		Q00234_FatesWhisper.class,
		Q00235_MimirsElixir.class,
		Q00237_WindsOfChange.class,
		Q00238_SuccessFailureOfBusiness.class,
		Q00239_WontYouJoinUs.class,
		Q00240_ImTheOnlyOneYouCanTrust.class,
		Q00241_PossessorOfAPreciousSoul1.class,
		Q00242_PossessorOfAPreciousSoul2.class,
		Q00246_PossessorOfAPreciousSoul3.class,
		Q00247_PossessorOfAPreciousSoul4.class,
		Q00249_PoisonedPlainsOfTheLizardmen.class,
		Q00250_WatchWhatYouEat.class,
		Q00251_NoSecrets.class,
		Q00252_ItSmellsDelicious.class,
		Q00254_LegendaryTales.class,
		Q00257_TheGuardIsBusy.class,
		Q00258_BringWolfPelts.class,
		Q00259_RequestFromTheFarmOwner.class,
		Q00260_OrcHunting.class,
		Q00261_CollectorsDream.class,
		Q00262_TradeWithTheIvoryTower.class,
		Q00263_OrcSubjugation.class,
		Q00264_KeenClaws.class,
		Q00265_BondsOfSlavery.class,
		Q00266_PleasOfPixies.class,
		Q00267_WrathOfVerdure.class,
		Q00268_TracesOfEvil.class,
		Q00269_InventionAmbition.class,
		Q00270_TheOneWhoEndsSilence.class,
		Q00271_ProofOfValor.class,
		Q00272_WrathOfAncestors.class,
		Q00273_InvadersOfTheHolyLand.class,
		Q00274_SkirmishWithTheWerewolves.class,
		Q00275_DarkWingedSpies.class,
		Q00276_TotemOfTheHestui.class,
		Q00277_GatekeepersOffering.class,
		Q00278_HomeSecurity.class,
		Q00279_TargetOfOpportunity.class,
		Q00280_TheFoodChain.class,
		Q00281_HeadForTheHills.class,
		Q00283_TheFewTheProudTheBrave.class,
		Q00284_MuertosFeather.class,
		Q00286_FabulousFeathers.class,
		Q00287_FiguringItOut.class,
		Q00288_HandleWithCare.class,
		Q00289_NoMoreSoupForYou.class,
		Q00290_ThreatRemoval.class,
		Q00291_RevengeOfTheRedbonnet.class,
		Q00292_BrigandsSweep.class,
		Q00293_TheHiddenVeins.class,
		Q00294_CovertBusiness.class,
		Q00295_DreamingOfTheSkies.class,
		Q00296_TarantulasSpiderSilk.class,
		Q00297_GatekeepersFavor.class,
		Q00298_LizardmensConspiracy.class,
		Q00300_HuntingLetoLizardman.class,
		Q00303_CollectArrowheads.class,
		Q00306_CrystalOfFireAndIce.class,
		Q00307_ControlDeviceOfTheGiants.class,
		Q00308_ReedFieldMaintenance.class,
		Q00309_ForAGoodCause.class,
		Q00310_OnlyWhatRemains.class,
		Q00311_ExpulsionOfEvilSpirits.class,
		Q00312_TakeAdvantageOfTheCrisis.class,
		Q00313_CollectSpores.class,
		Q00316_DestroyPlagueCarriers.class,
		Q00317_CatchTheWind.class,
		Q00319_ScentOfDeath.class,
		Q00320_BonesTellTheFuture.class,
		Q00324_SweetestVenom.class,
		Q00325_GrimCollector.class,
		Q00326_VanquishRemnants.class,
		Q00327_RecoverTheFarmland.class,
		Q00328_SenseForBusiness.class,
		Q00329_CuriosityOfADwarf.class,
		Q00331_ArrowOfVengeance.class,
		Q00337_AudienceWithTheLandDragon.class,
		Q00338_AlligatorHunter.class,
		Q00341_HuntingForWildBeasts.class,
		Q00344_1000YearsTheEndOfLamentation.class,
		Q00347_GoGetTheCalculator.class,
		Q00348_ArrogantSearch.class,
		Q00350_EnhanceYourWeapon.class,
		Q00354_ConquestOfAlligatorIsland.class,
		Q00357_WarehouseKeepersAmbition.class,
		Q00358_IllegitimateChildOfTheGoddess.class,
		Q00359_ForASleeplessDeadman.class,
		Q00360_PlunderTheirSupplies.class,
		Q00362_BardsMandolin.class,
		Q00363_SorrowfulSoundOfFlute.class,
		Q00364_JovialAccordion.class,
		Q00365_DevilsLegacy.class,
		Q00366_SilverHairedShaman.class,
		Q00367_ElectrifyingRecharge.class,
		Q00368_TrespassingIntoTheHolyGround.class,
		Q00369_CollectorOfJewels.class,
		Q00370_AnElderSowsSeeds.class,
		Q00376_ExplorationOfTheGiantsCavePart1.class,
		Q00377_ExplorationOfTheGiantsCavePart2.class,
		Q00380_BringOutTheFlavorOfIngredients.class,
		Q00381_LetsBecomeARoyalMember.class,
		Q00382_KailsMagicCoin.class,
		Q00385_YokeOfThePast.class,
		Q00401_PathToWarrior.class,
		Q00402_PathOfTheHumanKnight.class,
		Q00403_PathOfTheRogue.class,
		Q00404_PathOfTheHumanWizard.class,
		Q00405_PathOfTheCleric.class,
		Q00406_PathOfTheElvenKnight.class,
		Q00407_PathOfTheElvenScout.class,
		Q00408_PathOfTheElvenWizard.class,
		Q00409_PathOfTheElvenOracle.class,
		Q00410_PathOfThePalusKnight.class,
		Q00411_PathOfTheAssassin.class,
		Q00412_PathOfTheDarkWizard.class,
		Q00413_PathOfTheShillienOracle.class,
		Q00416_PathOfTheOrcShaman.class,
		Q00420_LittleWing.class,
		Q00421_LittleWingsBigAdventure.class,
		Q00423_TakeYourBestShot.class,
		Q00431_WeddingMarch.class,
		Q00432_BirthdayPartySong.class,
		Q00450_GraveRobberRescue.class,
		Q00451_LuciensAltar.class,
		Q00452_FindingtheLostSoldiers.class,
		Q00453_NotStrongEnoughAlone.class,
		Q00455_WingsOfSand.class,
		Q00456_DontKnowDontCare.class,
		Q00457_LostAndFound.class,
		Q00458_PerfectForm.class,
		Q00461_RumbleInTheBase.class,
		Q00463_IMustBeaGenius.class,
		Q00464_Oath.class,
		Q00503_PursuitClanAmbition.class,
		Q00504_CompetitionForTheBanditStronghold.class,
		Q00508_AClansReputation.class,
		Q00509_AClansFame.class,
		Q00510_AClansPrestige.class,
		Q00511_AwlUnderFoot.class,
		Q00512_BladeUnderFoot.class,
		Q00551_OlympiadStarter.class,
		Q00552_OlympiadVeteran.class,
		Q00553_OlympiadUndefeated.class,
		Q00601_WatchingEyes.class,
		Q00602_ShadowOfLight.class,
		Q00603_DaimonTheWhiteEyedPart1.class,
		Q00605_AllianceWithKetraOrcs.class,
		Q00606_BattleAgainstVarkaSilenos.class,
		Q00607_ProveYourCourageKetra.class,
		Q00608_SlayTheEnemyCommanderKetra.class,
		Q00609_MagicalPowerOfWaterPart1.class,
		Q00610_MagicalPowerOfWaterPart2.class,
		Q00611_AllianceWithVarkaSilenos.class,
		Q00612_BattleAgainstKetraOrcs.class,
		Q00613_ProveYourCourageVarka.class,
		Q00614_SlayTheEnemyCommanderVarka.class,
		Q00615_MagicalPowerOfFirePart1.class,
		Q00616_MagicalPowerOfFirePart2.class,
		Q00617_GatherTheFlames.class,
		Q00618_IntoTheFlame.class,
		Q00619_RelicsOfTheOldEmpire.class,
		Q00621_EggDelivery.class,
		Q00622_SpecialtyLiquorDelivery.class,
		Q00623_TheFinestFood.class,
		Q00624_TheFinestIngredientsPart1.class,
		Q00625_TheFinestIngredientsPart2.class,
		Q00626_ADarkTwilight.class,
		Q00627_HeartInSearchOfPower.class,
		Q00628_HuntGoldenRam.class,
		Q00629_CleanUpTheSwampOfScreams.class,
		Q00631_DeliciousTopChoiceMeat.class,
		Q00632_NecromancersRequest.class,
		Q00633_InTheForgottenVillage.class,
		Q00634_InSearchOfFragmentsOfDimension.class,
		Q00635_IntoTheDimensionalRift.class,
		Q00636_TruthBeyond.class,
		Q00637_ThroughOnceMore.class,
		Q00638_SeekersOfTheHolyGrail.class,
		Q00639_GuardiansOfTheHolyGrail.class,
		Q00641_AttackSailren.class,
		Q00642_APowerfulPrimevalCreature.class,
		Q00643_RiseAndFallOfTheElrokiTribe.class,
		Q00644_GraveRobberAnnihilation.class,
		Q00645_GhostsOfBatur.class,
		Q00646_SignsOfRevolt.class,
		Q00647_InfluxOfMachines.class,
		Q00648_AnIceMerchantsDream.class,
		Q00649_ALooterAndARailroadMan.class,
		Q00650_ABrokenDream.class,
		Q00651_RunawayYouth.class,
		Q00652_AnAgedExAdventurer.class,
		Q00653_WildMaiden.class,
		Q00654_JourneyToASettlement.class,
		Q00655_AGrandPlanForTamingWildBeasts.class,
		Q00659_IdRatherBeCollectingFairyBreath.class,
		Q00660_AidingTheFloranVillage.class,
		Q00661_MakingTheHarvestGroundsSafe.class,
		Q00662_AGameOfCards.class,
		Q00688_DefeatTheElrokianRaiders.class,
		Q00690_JudesRequest.class,
		Q00691_MatrasSuspiciousRequest.class,
		Q00692_HowtoOpposeEvil.class,
		Q00693_DefeatingDragonkinRemnants.class,
		Q00694_BreakThroughTheHallOfSuffering.class,
		Q00695_DefendtheHallofSuffering.class,
		Q00696_ConquertheHallofErosion.class,
		Q00697_DefendtheHallofErosion.class,
		Q00698_BlocktheLordsEscape.class,
		Q00699_GuardianOfTheSkies.class,
		Q00700_CursedLife.class,
		Q00701_ProofOfExistence.class,
		Q00702_ATrapForRevenge.class,
		Q00726_LightwithintheDarkness.class,
		Q00727_HopeWithinTheDarkness.class,
		Q00901_HowLavasaurusesAreMade.class,
		Q00902_ReclaimOurEra.class,
		Q00903_TheCallOfAntharas.class,
		Q00904_DragonTrophyAntharas.class,
		Q00905_RefinedDragonBlood.class,
		Q00906_TheCallOfValakas.class,
		Q00907_DragonTrophyValakas.class,
		Q00998_FallenAngelSelect.class,
		Q00999_T1Tutorial.class,
		Q10267_JourneyToGracia.class,
		Q10268_ToTheSeedOfInfinity.class,
		Q10269_ToTheSeedOfDestruction.class,
		Q10270_BirthOfTheSeed.class,
		Q10271_TheEnvelopingDarkness.class,
		Q10272_LightFragment.class,
		Q10273_GoodDayToFly.class,
		Q10274_CollectingInTheAir.class,
		Q10275_ContainingTheAttributePower.class,
		Q10276_MutatedKaneusGludio.class,
		Q10277_MutatedKaneusDion.class,
		Q10278_MutatedKaneusHeine.class,
		Q10279_MutatedKaneusOren.class,
		Q10280_MutatedKaneusSchuttgart.class,
		Q10281_MutatedKaneusRune.class,
		Q10282_ToTheSeedOfAnnihilation.class,
		Q10283_RequestOfIceMerchant.class,
		Q10284_AcquisitionOfDivineSword.class,
		Q10285_MeetingSirra.class,
		Q10286_ReunionWithSirra.class,
		Q10287_StoryOfThoseLeft.class,
		Q10288_SecretMission.class,
		Q10289_FadeToBlack.class,
		Q10290_LandDragonConqueror.class,
		Q10291_FireDragonDestroyer.class,
		Q10292_SevenSignsGirlofDoubt.class,
		Q10293_SevenSignsForbiddenBook.class,
		Q10294_SevenSignToTheMonastery.class,
		Q10295_SevenSignsSolinasTomb.class,
		Q10296_SevenSignsPowerOfTheSeal.class,
		Q10501_ZakenEmbroideredSoulCloak.class,
		Q10502_FreyaEmbroideredSoulCloak.class,
		Q10503_FrintezzaEmbroideredSoulCloak.class,
		Q10504_JewelOfAntharas.class,
		Q10505_JewelOfValakas.class
	};
	
	public static void main(String[] args)
	{
		for (Class<?> quest : QUESTS)
		{
			try
			{
				quest.newInstance();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, QuestMasterHandler.class.getSimpleName() + ": Failed loading " + quest.getSimpleName() + ":", e);
			}
		}
	}
}
