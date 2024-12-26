package brikks.view.container;

public record GameText(
        String backVariant,
        String goToMainMenuOnTap,
        String choiceInRange,
        String inputValidChoice,

        String menu,
        String[] menuVariants,

        String liderboard,

        String askUseExistingPlayer,

        String askDifficulty,
        String[] difficultyVariants,

        String askDuel,

        String askPlayerCount,

        String askName,

        String askChoiceSave,

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

        String askPlacingSpot,

        String askDoing,
        String[] doingVariants,

        String askRotation,

        String askChoiceX,
        String askChoiceY
) {}
