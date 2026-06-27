import * as fabric from 'fabric';

export interface LineRule {
  id?: number; 
  when: string;      // "(RS0 & RS1)"
  lines: string[];   // ["LG1", "LG2"]
  stroke: string;    // "red"
  priority: number;
}

function evalCondition(when: string, closed: Set<string>): boolean {
  if (!/^[\w\s&|!()]+$/.test(when)) {
    console.warn('⚠️ Wrong condition skipped :', when);
    return false;
  }
  const js = when
    // token ko UPPERCASE karke closed.has() me badlo -> rs8 / RS8 dono chalenge
    .replace(/[A-Za-z][A-Za-z0-9]*/g, (tok) => `c.has('${tok.toUpperCase()}')`)
    .replace(/&&?/g, ' && ')    // & ya && -> &&
    .replace(/\|\|?/g, ' || ');  // | ya || -> ||  (YAHI bug tha)

  console.log('🧪 when:', when, '=> js:', js);
  
  try {
    return Function('c', `return (${js});`)(closed) === true;
  } catch (e) {
    console.warn('Condition eval fail:', when, e);
    return false;
  }
}

export function applyRulesToCanvas(
  canvas: fabric.Canvas,
  closed: Set<string>,
  rules: LineRule[]
): void {
  // 1) saari colored lines ko original color par reset
  canvas.getObjects().forEach(o => {
    const t = (o as any).customType;
    if (t === 'lineG' || t === 'lineR') {
      o.set('stroke', (o as any).originalStroke || (t === 'lineR' ? 'red' : 'green'));
    }
  });

  // 2) priority order me matching rules apply karo
  [...rules]
    .sort((a, b) => (a.priority || 0) - (b.priority || 0))
    .forEach(rule => {
      if (!evalCondition(rule.when, closed)) return;
      rule.lines.forEach(lineId => {
        const line = canvas.getObjects().find(o => (o as any).customId === lineId);
        if (line) line.set('stroke', rule.stroke || 'red');
      });
    });

  canvas.renderAll();
}