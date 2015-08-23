uniform mat4 uMVPMatrix;

attribute vec2 vPosition;
attribute vec3 vColor;
attribute vec2 vTextCoords;

varying vec3 color;
varying vec2 tCoords;

void main(){
    color = vColor;
    tCoords = vTextCoords;
    gl_Position = uMVPMatrix * vec4(vPosition, 0.0, 1.0);
}