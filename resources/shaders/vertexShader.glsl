#version 450 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main(void) {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    pass_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz; // swizzle it! get the xyz components from the resulting 4d vector
    toLightVector = lightPosition - worldPosition.xyz;  // world position is a 4d vector. again, use a swizzle to get a 3d vector from it
}
