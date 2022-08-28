import React, { useEffect, useState } from "react"
import { observer } from "mobx-react-lite"
import { useNavigate } from "react-router-dom"
import { useCurrentGame } from "../../../hooks/api/useCurrentGame"
import { Paths } from "../routers/Paths"
import { Game } from "../../../generated"
import { Loading } from "../../ui/Loading"
import { Room } from "../../views/game/room/Room"
import GameView from "../../views/game/game/GameView"
import { GameSummary } from "../../views/game/gamesummary/GameSummary"
import BaseContainer from "../../ui/BaseContainer"
import SendIcon from "@mui/icons-material/Send"
import Button from "@mui/material/Button"

const gameStateEnum = Game.gameState

export type GameGuardProps = {
  redirectTo: Paths
}

export const GameGuard = observer((props: GameGuardProps) => {
  const navigate = useNavigate()
  const { game } = useCurrentGame()

  const [startRender] = useState(new Date())

  const mountedSinceOneSecond =
    new Date().getDate() - startRender.getDate() > 1000

  const [cachedGameState, setCachedGameState] = useState(undefined)
  const [runningGameStateUpdate, setRunningGameStateUpdate] =
    useState<NodeJS.Timeout>(undefined)

  const { startPollGame } = useCurrentGame()

  const goBack = () => {
    navigate(props.redirectTo)
  }

  const gameState = game?.gameState
  useEffect(
    () => {
      const pollGameSubscription = startPollGame()
      if (game) {
        if (
          cachedGameState === gameStateEnum.PLAYING &&
          gameState === gameStateEnum.CLOSED
        ) {
          setRunningGameStateUpdate((previousState) => {
            if (!previousState) {
              return setTimeout(() => setCachedGameState(gameState), 2000)
            }
            return previousState
          })
        } else {
          setCachedGameState(gameState)
        }
        return () => {
          pollGameSubscription.cancel()
          if (runningGameStateUpdate) {
            clearTimeout(runningGameStateUpdate)
          }
        }
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [cachedGameState, gameState]
  )

  return (
    <Loading ready={!!game || mountedSinceOneSecond}>
      <>
        {(() => {
          if (!game && mountedSinceOneSecond) {
            return (
              <BaseContainer>
                <h1> Game not found </h1>
                <Button
                  variant="contained"
                  endIcon={<SendIcon />}
                  onClick={goBack}
                >
                  Go back
                </Button>
              </BaseContainer>
            )
          }
          if (cachedGameState === gameStateEnum.FINDING_PLAYERS) {
            return <Room />
          }
          if (cachedGameState === gameStateEnum.PLAYING) {
            return <GameView />
          }
          if (cachedGameState === gameStateEnum.CLOSED) {
            return <GameSummary />
          }
        })()}
      </>
    </Loading>
  )
})
