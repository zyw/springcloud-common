import CustomPalette from "@/components/custom-palette/custom/CustomPalette";
import CustomRenderer from "@/components/custom-renderer/custom/CustomRenderer";

export default {
    __init__: ['customPalette','customRenderer'],
    customPalette: ['type', CustomPalette],
    customRenderer: ['type', CustomRenderer]
}