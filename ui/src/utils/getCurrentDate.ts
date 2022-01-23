export function getCurrentDate(): string {
    return new Date().toISOString().substring(0, 10)
}