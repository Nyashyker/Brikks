package brikks.view.container;

public record GameText(
        String backVariant,
        String goToMainMenuOnTap,
        String choiceInRange,
        String inputValidChoice,

        String menu,
        String[] menuVariants,

        String leaderboard,

        String askUseExistingPlayer,

        String askDifficulty,
        String[] difficultyVariants,

        String askDuel,

        String askPlayerCount,

        String askName,
        String nameToLong,

        String askChoiceSave,

        String max,
        String bonusScore,
        String energy,
        String bombs,

        String end,

        String endSolo,
        String[] ranks,

        String endStandard,

        String endDuel,

        String exit,


        String askReroll,

        String blockPosition,
        String askPlacingSpot,

        String askDeed,
        String[] deedVariants,

        String askRotation,

        String askChoiceX,
        String askChoiceY,

        String failPlace,
        String failBomb,
        String failRotation,
        String failChoice,
        String failGiveUp,
        String fail,


        String askPlacingSpotDuel
) {}
