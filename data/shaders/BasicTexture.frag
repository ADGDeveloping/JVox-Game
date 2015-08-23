uniform sampler2D texture;

varying vec3 color;
varying vec2 tCoords;

void main(){
    vec4 tex = texture2D(texture, tCoords);
    vec3 pColor = tex.rgb * color;
    gl_FragColor = vec4(pColor, tex.a);
}