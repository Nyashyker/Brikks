package brikks.view;

public record GameText(
        String exitVariant,
        String choiceInRange,
        String inputValidNumber,
        String inputValidChoice,

        String menu,
        String[] menuVariants,

        String liderboard,
        String liderboardExit,

        String askUseExistingPlayer,

        String askDifficulty,
        String[] difficultyVariants,

        String askDuel,

        String askPlayerCount,

        String askName,

        String askChoiceSave,

        String exit
) {}
