import { Link } from "../generated"
import { getDomain } from "../api/api"

export function toIri(link: Link) {
  const apiBasePath = new URL(getDomain())
  let toReplace = apiBasePath.protocol + "//" + apiBasePath.hostname
  if (apiBasePath.port.length > 0) {
    toReplace += ":" + apiBasePath.port
  }
  const removedDomain = link.href.replace(toReplace, "")
  if (!link.templated) {
    return removedDomain
  }
  return removedDomain.replace("{?projection}", "?projection=embed")
}
