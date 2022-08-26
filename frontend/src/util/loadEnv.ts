// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
if (window?.env?.INSERT_ENV_HERE === "") {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  window.env = process.env
}

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
export const env = { ...window.env }
