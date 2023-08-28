package com.tatam.materialcalculator.domain

class ExpressionParser(
    private val calculation: String
) {

    fun parse(): List<ExpressionPart> {
        val result = mutableListOf<ExpressionPart>()
        var i = 0
        while (i < calculation.length) {
            val curChar = calculation[i]
            when {
                curChar in operationSymbols -> {
                    result.add(
                        ExpressionPart.Op(operationFromSymbol(curChar))
                    )
                }
                curChar.isDigit() -> {
                    i = parseNumber(i, result)
                    continue
                }
                curChar in "()" -> {
                    parseParenthesis(curChar, result)
                }
            }
            i++
        }
        return result
    }

    private fun parseParenthesis(curChar: Char, result: MutableList<ExpressionPart>) {
        result.add(
            ExpressionPart.Parenthesis(
                type = when(curChar) {
                    '(' -> ParenthesisType.Opening
                    ')' -> ParenthesisType.Closing
                    else -> throw IllegalArgumentException("Invalid parenthesis type")
                }
            )
        )
    }

    private fun parseNumber(startingIndex: Int, result: MutableList<ExpressionPart>): Int {
        var i = startingIndex
        val numberAsString = buildString {
            while(i < calculation.length && calculation[i] in "1234567890.") {
                append(calculation[i])
                i++
            }
        }
        result.add(ExpressionPart.Number(numberAsString.toDouble()))
        return i
    }
}