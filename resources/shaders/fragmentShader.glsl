#version 450 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal); // normalize makes the size of the vector = 1. Only direction of the vector matters here. Size is irrelevant
    vec3 unitLightVector = normalize(toLightVector);

    float nDotl = dot(unitNormal, unitLightVector); // dot product calculation of 2 vectors. nDotl is how bright this pixel should be. difference of the position and normal vector to the light source
    float brightness = max(nDotl, 0.0); // clamp the brightness result value to between 0 and 1. values less than 0 are clamped to 0
    vec3 diffuse = brightness * lightColor; // calculate final color of this pixel by how much light it has

    out_Color = vec4(diffuse, 1.0) *  texture(modelTexture, pass_textureCoords);        // returns color of the pixel from the texture at specified texture coordinates
}
