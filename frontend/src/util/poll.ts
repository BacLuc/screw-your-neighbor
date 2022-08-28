type PollInfo = {
  intervals: number[]
  timer: NodeJS.Timer
}

const pollerMap = new Map<string, PollInfo>()

export function poll(
  identifier: string,
  pollFunction: () => Promise<void>,
  interval: number
) {
  let finished = false
  const timerFunction = () => {
    if (finished) {
      finished = false
      pollFunction().finally(() => (finished = true))
    }
  }

  const pollInfo = pollerMap.get(identifier)
  const intervalSmaller =
    pollInfo?.intervals.every((value) => value > interval) ?? false

  if (intervalSmaller) {
    clearInterval(pollInfo.timer)
  }
  let newTimer = pollInfo?.timer
  if (!pollInfo || intervalSmaller) {
    pollFunction().finally(() => (finished = true))
    newTimer = setInterval(timerFunction, interval)
  }
  const intervalsBefore = pollInfo?.intervals || []
  const newIntervals = [...intervalsBefore, interval]

  pollerMap.set(identifier, {
    timer: newTimer,
    intervals: newIntervals,
  })

  return {
    cancel: () => {
      const cancelPollInfo = pollerMap.get(identifier)
      if (!cancelPollInfo) {
        console.warn("this should not happen")
        return
      }
      if (cancelPollInfo.intervals.length === 1) {
        clearInterval(cancelPollInfo.timer)
        pollerMap.delete(identifier)
        return
      }

      let alreadyRemoved = false
      const intervalsWithOneLess = cancelPollInfo.intervals.filter((value) => {
        if (!alreadyRemoved && value === interval) {
          alreadyRemoved = true
          return false
        }
        return true
      })

      const wasSmallestInterval = intervalsWithOneLess.every(
        (value) => value > interval
      )
      if (wasSmallestInterval) {
        const newInterval = Math.min(...intervalsWithOneLess)
        clearInterval(cancelPollInfo.timer)
        pollerMap.set(identifier, {
          intervals: intervalsWithOneLess,
          timer: setInterval(timerFunction, newInterval),
        })
        return
      }
      pollerMap.set(identifier, {
        intervals: intervalsWithOneLess,
        timer: cancelPollInfo.timer,
      })
    },
  }
}
