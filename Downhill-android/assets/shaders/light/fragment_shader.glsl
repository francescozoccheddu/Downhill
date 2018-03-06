#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

const LOW vec3 color = vec3 ( 1.0 , 1.0 , 0.0 );

uniform LOW float u_length;
varying LOW vec2 v_position;
varying LOW float v_alpha;

void main()
{
	LOW float delta = 1.0 - length( v_position ) / u_length;
	LOW float delta2 = 1.0 - ( ( v_position.y < 0.0 ? - v_position.y : v_position.y ) / v_position.x );
	gl_FragColor = vec4 ( color , delta * delta2 * v_alpha );
}
