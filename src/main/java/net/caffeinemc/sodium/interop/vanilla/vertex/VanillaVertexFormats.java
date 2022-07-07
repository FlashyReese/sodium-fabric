package net.caffeinemc.sodium.interop.vanilla.vertex;

import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionTextureSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionTextureType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.glyph.GlyphVertexSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.glyph.GlyphVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.line.LineVertexSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.line.LineVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.particle.ParticleVertexSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.particle.ParticleVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad.ModelQuadVertexSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad.ModelQuadVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.screen.BasicScreenQuadVertexSink;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.screen.BasicScreenQuadVertexType;

public class VanillaVertexFormats {
    public static final VanillaVertexType<ModelQuadVertexSink> QUADS = new ModelQuadVertexType();
    public static final VanillaVertexType<LineVertexSink> LINES = new LineVertexType();
    public static final VanillaVertexType<GlyphVertexSink> GLYPHS = new GlyphVertexType();
    public static final VanillaVertexType<ParticleVertexSink> PARTICLES = new ParticleVertexType();
    public static final VanillaVertexType<BasicScreenQuadVertexSink> BASIC_SCREEN_QUADS = new BasicScreenQuadVertexType();
    public static final VanillaVertexType<PositionColorSink> POSITION_COLOR = new PositionColorType();
    public static final VanillaVertexType<PositionTextureSink> POSITION_TEXTURE = new PositionTextureType();
}
