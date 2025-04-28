package lotto.domain

import lotto.Lotto

class LottoResultChecker {

    private fun matchTicketWithWinningNumbers(
            ticket: Lotto,
            winningNumbers: Lotto,
            bonusNumber: Int
    ): MatchResult {
        // 로또 한 장에 대해서 몇 개의 번호가 맞았는지 확인, 보너스 번호 포함 여부 확인
        val matchCount = ticket.getNumbers.count { winningNumbers.getNumbers.contains(it) }
        val hasBonusNumber = ticket.getNumbers.contains(bonusNumber)

        return MatchResult(matchCount, hasBonusNumber)
    }

    private fun getWinningRank(matchResult: MatchResult): WinningPrizeRanks {
        // 매칭 결과에 따라 당첨 등수 반환
        return matchResult.getWinningRank()
    }

    fun checkLottoResults(
            tickets: List<Lotto>,
            winningNumbers: Lotto,
            bonusNumber: Int
    ): Map<WinningPrizeRanks, Int> {
        // 로또 티켓 리스트와 당첨 번호, 보너스 번호를 받아서 각 등수별 당첨 개수를 반환
        return tickets
                .groupBy { ticket ->
                    val matchResult =
                            matchTicketWithWinningNumbers(ticket, winningNumbers, bonusNumber)
                    getWinningRank(matchResult)
                }
                .mapValues { it.value.size }
    }

    fun calculateTotalPrize(winningCounts: Map<WinningPrizeRanks, Int>): Int {
        // 각 등수별 당첨 개수에 따른 총 당첨 금액 계산
        return winningCounts.entries.sumOf { (rank, count) -> rank.prize * count }
    }

    fun getProfitRate(totalPrize: Int, purchaseAmount: Int): Double {
        // 총 당첨 금액과 구매 금액을 받아서 수익률 계산
        return (totalPrize.toDouble() / purchaseAmount) * 100
    }
}
