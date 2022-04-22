import { customAlphabet } from "nanoid"

export let nanoTime = (): number => {
    if (typeof window !== 'undefined') {
        // 浏览器
        if (typeof window.performance !== 'undefined' && typeof performance.now !== 'undefined') {
            // support hrt
            return performance.now()
        } else {
            // oh no..
            return (new Date()).getTime()
        }
    } else {
        // node.js
        var diff = process.hrtime()
        return (diff[0] * 1e9 + diff[1]) / 1e6 // nano seconde -> ms
    }
    
}

const alphabet = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

// 生成消息ID
export let generateMsgId = (): string => {
    const nanoid = customAlphabet(alphabet, 15);
    return nanoid() //=> "xQXUazc78Mv2wNr"
}