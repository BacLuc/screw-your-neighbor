import { EntityModelPlayer } from "../generated"
import { action, makeObservable, observable } from "mobx"

export class PlayerStore {
  @observable me: EntityModelPlayer
  @observable players: Array<EntityModelPlayer>

  constructor() {
    this.me = null
    this.players = []
    makeObservable(this)
  }

  @action
  setMe(player?: EntityModelPlayer) {
    this.me = player
  }

  @action
  setPlayers(players: Array<EntityModelPlayer>) {
    this.players = players
  }
}
