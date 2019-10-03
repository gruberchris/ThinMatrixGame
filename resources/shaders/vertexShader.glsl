#version 410 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float useFakeLighting;
uniform float numberOfRowsInTextureAtlas;
uniform vec2 offset;

const float fogDensity = 0.007;
const float fogGradient = 1.5;

void main(void) {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCamera;
    pass_textureCoords = (textureCoords / numberOfRowsInTextureAtlas) + offset;

    vec3 actualNormal = normal;

    if (useFakeLighting > 0.5) {
        actualNormal = vec3(0.0, 1.0, 0.0); // set this normal to point directly up the y axis
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz; // swizzle it! get the xyz components from the resulting 4d vector
    toLightVector = lightPosition - worldPosition.xyz;  // world position is a 4d vector. again, use a swizzle to get a 3d vector from it

    // This shader does not have the "camera" position, but the view matrix position is the inverse of the camera, so the inverse of the view matrix is the camera position
    // multiply this matrix by an  empty 4d matrix and subract the worldPosition of the vertex gives the distance between them
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    // Calculate fog
    float distanceFromCamera = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distanceFromCamera * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
