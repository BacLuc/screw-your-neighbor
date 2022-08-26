import { AppClient } from "../generated"
import { env } from "../util/loadEnv"

const getDomain = () => env.REACT_APP_API_URL

const api = new AppClient({
  BASE: getDomain(),
  WITH_CREDENTIALS: true,
  HEADERS: {
    "Content-Type": "application/json",
  },
})

export { api, getDomain }
