
import CustomPalette from '@/components/custom-palette/custom/CustomPalette'
import CustomRenderer from '@/components/custom-renderer/custom/CustomRenderer'
import CustomContextPad from './CustomContextPad'

export default {
    __init__: ['customPalette', 'customRenderer', 'customContextPad'],
    customPalette: ['type', CustomPalette],
    customRenderer: ['type', CustomRenderer],
    customContextPad: ['type', CustomContextPad],
}