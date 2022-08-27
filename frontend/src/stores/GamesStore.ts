import { action, makeAutoObservable, observable } from "mobx"
import { EntityModelGame } from "../generated"

export class GamesStore {
  @observable games: Array<EntityModelGame> = []

  constructor() {
    makeAutoObservable(this)
  }

  @action
  setGames(games: Array<EntityModelGame>) {
    this.games = games
  }
}
