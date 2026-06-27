// circuit-logics.ts

import * as fabric from 'fabric';

export function mainCircuitLogic(canvas: fabric.Canvas, closedSignals: Set<string>) {
    const rs18_19_20_closed = ['RS18', 'RS19', 'RS20'].some(rs => closedSignals.has(rs));
    const rs18_19_20_lines = ['LG25', 'LG39', 'LG46', 'LG24', 'LG38', 'LG45'];
    const rs8_9_10_closed = ['RS8', 'RS9', 'RS10'].some(rs => closedSignals.has(rs));
    const rs11_closed = closedSignals.has('RS11');
    const rs1_closed = closedSignals.has('RS1');
    const rs3_closed = closedSignals.has('RS3');
    const rs4_closed = closedSignals.has('RS4');
    const rs39_closed = closedSignals.has('RS39');
    const rs12_closed = closedSignals.has('RS12');
    const rs2_closed = closedSignals.has('RS2');
    const rs15_closed = closedSignals.has('RS15');
    const rs46_closed = closedSignals.has('RS46');
    const rs47_closed = closedSignals.has('RS47');
    const rs11_main = ['LG7', 'LG8', 'LG11', 'LG12'];
    const rs26_closed = closedSignals.has('RS26');
    const rs38_closed = closedSignals.has('RS38');
    const rs37_closed = closedSignals.has('RS37');
    const rs45_closed = closedSignals.has('RS45');
    const rs11_lines = ['LG7', 'LG8', 'LG11', 'LG12', 'LG24', 'LG25', 'LG38', 'LG39', 'LG45', 'LG46'];

    canvas.getObjects().forEach(obj => {
        const anyObj = obj as any;
        if ((anyObj.customType === 'lineR' || anyObj.customType === 'lineG') && anyObj.customId) {
            let shouldBeRed = false;
            const lineId = anyObj.customId;

            // <=================== ======= RS8/RS9/RS10 LOGIC ======================================>
            if (rs11_closed && rs8_9_10_closed && ['LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) {
                shouldBeRed = true;
                if (rs1_closed && rs3_closed) {
                    if (['LG7', 'LG12', 'LG11', 'LG8'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs4_closed && rs12_closed && rs1_closed) {
                    if (['LG7', 'LG12', 'LG11', 'LG8'].includes(lineId)) shouldBeRed = true;
                }
                if (!rs1_closed || !rs3_closed) {
                    if (['LG7', 'LG12', 'LG11', 'LG8'].includes(lineId)) shouldBeRed = false;

                    if (!rs3_closed && rs4_closed && rs12_closed) {
                        if (['LG7', 'LG12',].includes(lineId)) shouldBeRed = false;
                        if (['LG8', 'LG11',].includes(lineId)) shouldBeRed = true;
                    }
                    if (!rs1_closed && rs4_closed && rs12_closed) {
                        if (['LG8', 'LG11',].includes(lineId)) shouldBeRed = false;
                        if (['LG7', 'LG12',].includes(lineId)) shouldBeRed = true;
                    }
                    if (!rs1_closed && !rs3_closed && rs4_closed && rs12_closed) {
                        if (['LG8', 'LG11',].includes(lineId)) shouldBeRed = false;
                        if (['LG7', 'LG12',].includes(lineId)) shouldBeRed = false;
                    }
                    if (closedSignals.has('RS5')) {
                        if (['LG11'].includes(lineId)) shouldBeRed = true;
                    }
                    if (closedSignals.has('RS14')) {
                        if (['LG12'].includes(lineId)) shouldBeRed = true;
                    }
                }
            }

            //=============================RS6/RS16 Logic=============================
            if (closedSignals.has('RS16')) {
                if (rs3_closed && rs4_closed && rs12_closed) {
                    if (['LG7', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (!rs3_closed && rs4_closed && rs12_closed) {
                    if (['LG7', 'LG12'].includes(lineId)) shouldBeRed = false;

                    if (closedSignals.has('RS14')) {
                        if (['LG12'].includes(lineId)) shouldBeRed = true;
                    }
                }
            }

            if (closedSignals.has('RS6')) {
                if (closedSignals.has('RS6') && closedSignals.has('RS16') && rs1_closed && rs3_closed) {
                    if (['LG8', 'LG11', 'LG7', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (rs4_closed && rs12_closed && rs1_closed) {
                    if (['LG8', 'LG11'].includes(lineId)) shouldBeRed = true;
                }
                if (rs4_closed && rs12_closed && !rs1_closed) {
                    if (['LG8', 'LG11'].includes(lineId)) shouldBeRed = false;
                    if (closedSignals.has('RS5')) {
                        if (['LG11'].includes(lineId)) shouldBeRed = true;
                    }
                }
            }

            //<------------------------------RS11 LOGIC Left Side------------------------------>
            if (!rs11_closed) {
                if (rs1_closed && rs3_closed && rs8_9_10_closed && (!rs18_19_20_closed || !rs47_closed || !rs46_closed)) {
                    if (['LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = false;
                }
                if (rs1_closed && rs3_closed && rs8_9_10_closed && (rs18_19_20_closed && rs47_closed && rs46_closed)) {
                    if (['LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (rs1_closed && rs4_closed && rs12_closed && closedSignals.has('RS5')) {
                    if (['LG8'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs4_closed && rs12_closed && closedSignals.has('RS14')) {
                    if (['LG7'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS16')) {
                    if (rs3_closed && rs4_closed && rs12_closed) {
                        if (['LG7', 'LG12'].includes(lineId)) shouldBeRed = true;
                    }
                    if (!rs3_closed && rs4_closed && rs12_closed) {
                        if (['LG7', 'LG12'].includes(lineId)) shouldBeRed = false;

                        if (closedSignals.has('RS14')) {
                            if (['LG12'].includes(lineId)) shouldBeRed = true;
                        }
                    }
                }

                if (closedSignals.has('RS6')) {
                    if (closedSignals.has('RS6') && closedSignals.has('RS16') && rs1_closed && rs3_closed) {
                        if (['LG8', 'LG11', 'LG7', 'LG12'].includes(lineId)) shouldBeRed = true;
                    }
                    if (rs4_closed && rs12_closed && rs1_closed) {
                        if (['LG8', 'LG11'].includes(lineId)) shouldBeRed = true;
                    }
                    if (rs4_closed && rs12_closed && !rs1_closed) {
                        if (['LG8', 'LG11'].includes(lineId)) shouldBeRed = false;
                        if (closedSignals.has('RS5')) {
                            if (['LG11'].includes(lineId)) shouldBeRed = true;
                        }
                    }
                }
            }

            //=============================Right Side Lines RS18/19/20 Logic=============================
            if (rs11_closed && rs18_19_20_closed && rs18_19_20_lines.includes(lineId)) {
                shouldBeRed = true;
                if (!rs47_closed || !rs46_closed) {
                    if (rs18_19_20_lines.includes(lineId)) {
                        shouldBeRed = false;
                    }
                    if (!rs46_closed && rs26_closed && rs38_closed && rs45_closed) {
                        if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = false;
                        if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = false;
                    }
                    if (!rs47_closed && rs26_closed && rs38_closed && rs45_closed) {
                        if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = false;
                        if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = false;
                    }
                    if (!rs47_closed && !rs46_closed && rs26_closed && rs38_closed && rs45_closed) {
                        if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = false;
                        if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = false;
                    }
                    if ((!rs47_closed || !rs46_closed) && closedSignals.has('RS27') && closedSignals.has('RS25')) {
                        if (['LG25', 'LG24'].includes(lineId)) shouldBeRed = true;
                    }
                    if ((!rs47_closed || !rs46_closed) && closedSignals.has('RS27') && rs26_closed && closedSignals.has('RS37')) {
                        if (['LG25', 'LG24', 'LG38'].includes(lineId)) shouldBeRed = true;
                    }
                    if ((!rs47_closed || !rs46_closed) && closedSignals.has('RS25') && rs26_closed && closedSignals.has('RS39')) {
                        if (['LG25', 'LG24', 'LG39'].includes(lineId)) shouldBeRed = true;
                    }
                    if ((!rs47_closed || !rs46_closed) && rs39_closed && rs37_closed) {
                        if (['LG24', 'LG38'].includes(lineId)) shouldBeRed = true;
                        if (['LG25', 'LG39',].includes(lineId)) shouldBeRed = true;
                    }
                    if (rs26_closed && rs37_closed && closedSignals.has('RS25')) {
                        if (['LG38'].includes(lineId)) shouldBeRed = true;
                    }
                    if (rs26_closed && rs39_closed && closedSignals.has('RS27')) {
                        if (['LG39'].includes(lineId)) shouldBeRed = true;
                    }
                    if (rs47_closed && rs46_closed) {
                        if (rs18_19_20_lines.includes(lineId)) {
                            shouldBeRed = true;
                        }
                    }
                    if (rs47_closed && !rs46_closed && rs38_closed && rs45_closed && rs37_closed) {
                        if (rs18_19_20_lines.includes(lineId)) {
                            shouldBeRed = true;
                            if (['LG45'].includes(lineId)) shouldBeRed = false;
                        }
                    }
                    if (rs47_closed && !rs46_closed && rs38_closed && rs45_closed && closedSignals.has('RS26') && closedSignals.has('RS25')) {
                        if (rs18_19_20_lines.includes(lineId)) {
                            shouldBeRed = true;
                            if (['LG45', 'LG38'].includes(lineId)) shouldBeRed = false;
                        }
                    }
                }
            }

            //=============================RS15/RS17 Logic=============================
            if (rs15_closed) {
                if (rs38_closed && rs26_closed && rs45_closed && rs46_closed) {
                    if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (!rs46_closed && rs26_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = false;
                    if (!rs46_closed && rs26_closed && rs38_closed && rs45_closed && rs37_closed) {
                        if (['LG24', 'LG38'].includes(lineId)) shouldBeRed = true;
                    }
                }
                if (!rs46_closed && rs26_closed && rs38_closed && rs45_closed && rs15_closed && closedSignals.has('RS25')) {
                    if (['LG24'].includes(lineId)) shouldBeRed = true;
                }
            }

            if (closedSignals.has('RS17')) {
                if (rs26_closed && rs38_closed && rs45_closed) {
                    if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (!rs47_closed && rs26_closed && rs38_closed && rs45_closed) {
                    if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = false;
                }
                if (!rs47_closed && rs26_closed && rs38_closed && rs45_closed && rs39_closed) {
                    if (['LG25', 'LG39'].includes(lineId)) shouldBeRed = true;
                }
                if (!rs47_closed && rs26_closed && rs38_closed && rs45_closed && closedSignals.has('RS27')) {
                    if (['LG25'].includes(lineId)) shouldBeRed = true;
                }
            }

            //=============================RS11 Logic Right Side=============================

            if (!rs11_closed && rs18_19_20_closed && rs46_closed && rs47_closed && (!rs8_9_10_closed || (!rs1_closed || !rs3_closed))) {
                if (['LG24', 'LG25', 'LG38', 'LG39', 'LG45', 'LG46'].includes(lineId)) shouldBeRed = false;
                if (rs26_closed && rs39_closed && closedSignals.has('RS27')) {
                    if (['LG39'].includes(lineId)) shouldBeRed = true;
                }
                if (rs26_closed && rs38_closed && rs45_closed && rs15_closed) {
                    if (['LG24', 'LG38', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (rs26_closed && rs38_closed && rs45_closed && closedSignals.has('RS17')) {
                    if (['LG25', 'LG39', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (rs26_closed && rs38_closed && rs45_closed && closedSignals.has('RS25')) {
                    if (['LG38', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (rs26_closed && rs38_closed && rs45_closed && closedSignals.has('RS27')) {
                    if (['LG39', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
            }
            if (rs26_closed && rs37_closed && closedSignals.has('RS25')) {
                if (['LG38'].includes(lineId)) shouldBeRed = true;
            }
            if (rs26_closed && rs39_closed && closedSignals.has('RS27')) {
                if (['LG39'].includes(lineId)) shouldBeRed = true;
            }
            if (rs38_closed && rs45_closed && rs47_closed && closedSignals.has('RS27') && closedSignals.has('RS26')) {
                if (['LG46', 'LG39'].includes(lineId)) shouldBeRed = true;
            }
            if (rs38_closed && rs45_closed && rs46_closed && closedSignals.has('RS25') && closedSignals.has('RS26')) {
                if (['LG45', 'LG38'].includes(lineId)) shouldBeRed = true;
            }
            if (closedSignals.has('RS25') && rs26_closed && closedSignals.has('RS27') && rs38_closed && rs37_closed && rs39_closed) {
                if (['LG38', 'LG39'].includes(lineId)) shouldBeRed = true;
            }
            if (!rs11_closed && rs18_19_20_closed && rs46_closed && rs47_closed && rs8_9_10_closed && rs1_closed && rs3_closed) {
                if (['LG24', 'LG25', 'LG38', 'LG39', 'LG45', 'LG46'].includes(lineId)) shouldBeRed = true;
            }

            //================================================================Compulsury logic ==============================================================
            if (rs8_9_10_closed && !rs11_closed && rs18_19_20_closed) {
                if (closedSignals.has('RS14') && closedSignals.has('RS5') && closedSignals.has('RS25') && closedSignals.has('RS27')) {
                    if (['LG24', 'LG25', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS14') && closedSignals.has('RS5') && rs39_closed && rs37_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS14') && closedSignals.has('RS5') && rs46_closed && rs47_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG11', 'LG12', 'LG45', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs1_closed && rs37_closed && rs39_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs1_closed && closedSignals.has('RS27') && closedSignals.has('RS25')) {
                    if (['LG24', 'LG25', 'LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs1_closed && rs46_closed && rs39_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG7', 'LG8', 'LG11', 'LG12', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS5') && closedSignals.has('RS14') && rs46_closed && rs39_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG11', 'LG12', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS5') && closedSignals.has('RS14') && rs47_closed && rs37_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG39', 'LG11', 'LG12', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS27') && rs46_closed && rs38_closed && rs45_closed && rs26_closed && closedSignals.has('RS26') && rs1_closed && rs3_closed) {
                    if (['LG24', 'LG25', 'LG38', 'LG7', 'LG8', 'LG11', 'LG12', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS27') && rs46_closed && rs38_closed && rs45_closed && rs26_closed && closedSignals.has('RS26') && closedSignals.has('RS5') && closedSignals.has('RS14')) {
                    if (['LG24', 'LG25', 'LG38', 'LG11', 'LG12', 'LG45'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs1_closed && rs26_closed && closedSignals.has('RS25') && rs47_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG39', 'LG7', 'LG8', 'LG11', 'LG12', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (closedSignals.has('RS5') && closedSignals.has('RS14') && rs26_closed && closedSignals.has('RS25') && rs47_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG39', 'LG11', 'LG12', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if (rs3_closed && rs1_closed && rs37_closed && rs47_closed && rs38_closed && rs45_closed) {
                    if (['LG24', 'LG25', 'LG39', 'LG38', 'LG7', 'LG8', 'LG11', 'LG12', 'LG46'].includes(lineId)) shouldBeRed = true;
                }
                if ((!rs46_closed || !rs47_closed) && rs1_closed && rs3_closed && !rs45_closed && rs38_closed && rs37_closed && rs26_closed && closedSignals.has('RS27')) {
                    if (['LG24', 'LG25', 'LG38', 'LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if ((!rs46_closed || !rs47_closed) && closedSignals.has('RS5') && closedSignals.has('RS14') && rs3_closed && !rs45_closed && rs38_closed && rs37_closed && rs26_closed && closedSignals.has('RS27')) {
                    if (['LG24', 'LG25', 'LG38', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if ((!rs46_closed || !rs47_closed) && rs1_closed && rs3_closed && !rs45_closed && rs38_closed && rs26_closed && rs26_closed && closedSignals.has('RS25')) {
                    if (['LG24', 'LG25', 'LG39', 'LG7', 'LG8', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
                if ((!rs46_closed || !rs47_closed) && closedSignals.has('RS5') && closedSignals.has('RS14') && !rs45_closed && rs38_closed && rs39_closed && rs26_closed && closedSignals.has('RS25')) {
                    if (['LG24', 'LG25', 'LG39', 'LG11', 'LG12'].includes(lineId)) shouldBeRed = true;
                }
            }

            if (rs15_closed && closedSignals.has('RS27') && rs46_closed && rs47_closed) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
                if (['LG25'].includes(lineId)) shouldBeRed = false;
            }

            if (closedSignals.has('RS25') && closedSignals.has('RS17') && rs46_closed && rs47_closed) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
                if (['LG24'].includes(lineId)) shouldBeRed = false;
            }

            if (rs15_closed && rs39_closed && rs26_closed && rs46_closed && rs47_closed) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
                if (['LG25', 'LG39'].includes(lineId)) shouldBeRed = false;
            }

            if (rs1_closed && rs4_closed && rs12_closed && closedSignals.has('RS5')) {
                if (['LG8'].includes(lineId)) shouldBeRed = true;
            }

            if (rs3_closed && rs4_closed && rs12_closed && closedSignals.has('RS14')) {
                if (['LG7'].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS13')) {
                if (['LG13'].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS14') && closedSignals.has('RS16')) {
                if (['LG12'].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS6') && closedSignals.has('RS5')) {
                if (['LG11'].includes(lineId)) shouldBeRed = true;
            }

            if (rs46_closed && rs38_closed && rs45_closed && rs37_closed) {
                if (['LG45'].includes(lineId)) shouldBeRed = true;
            }

            if (rs47_closed && rs38_closed && rs45_closed && rs39_closed) {
                if (['LG46'].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS25') && rs15_closed) {
                if (['LG24'].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS27') && closedSignals.has('RS17')) {
                if (['LG25'].includes(lineId)) shouldBeRed = true;
            }

            if (rs15_closed && closedSignals.has('RS17') && rs46_closed && rs47_closed) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
            }

            if (rs47_closed && rs46_closed && closedSignals.has('RS27') && closedSignals.has('RS25')) {
                if (['LG45', 'LG38'].includes(lineId)) shouldBeRed = true;
                if (['LG46', 'LG39',].includes(lineId)) shouldBeRed = true;
            }

            if (rs15_closed && rs39_closed && rs37_closed && closedSignals.has('RS17')) {
                if (['LG25', 'LG24'].includes(lineId)) shouldBeRed = true;
                if (['LG38', 'LG39',].includes(lineId)) shouldBeRed = true;
            }

            if (closedSignals.has('RS25') && closedSignals.has('RS27') && rs37_closed && rs39_closed) {
                if (['LG39', 'LG38'].includes(lineId)) shouldBeRed = true;
            }

            if (rs37_closed && rs39_closed && rs46_closed && rs47_closed) {
                if (['LG45', 'LG46'].includes(lineId)) shouldBeRed = true;
            }

            if (!rs47_closed && rs38_closed && rs39_closed && rs45_closed && rs46_closed && ((rs18_19_20_closed && rs11_closed) || rs15_closed && closedSignals.has('RS17'))) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
                if (['LG46'].includes(lineId)) shouldBeRed = false;
            }

            if (rs47_closed && rs38_closed && rs37_closed && rs45_closed && !rs46_closed && ((rs18_19_20_closed && rs11_closed) || rs15_closed && closedSignals.has('RS17'))) {
                if (rs18_19_20_lines.includes(lineId)) {
                    shouldBeRed = true;
                }
                if (['LG45'].includes(lineId)) shouldBeRed = false;
            }

            if (rs1_closed && rs3_closed && closedSignals.has('RS5') && closedSignals.has('RS14')) {
                if (['LG7', 'LG8'].includes(lineId)) shouldBeRed = true;
            }

            if (rs46_closed && rs47_closed && rs37_closed && rs26_closed && closedSignals.has('RS27')) {
                if (['LG45', 'LG46', 'LG39'].includes(lineId)) shouldBeRed = true;
            }

            if (rs46_closed && rs47_closed && rs39_closed && rs26_closed && closedSignals.has('RS25')) {
                if (['LG45', 'LG46', 'LG38'].includes(lineId)) shouldBeRed = true;
            }

            anyObj.set('stroke', shouldBeRed ? 'red' : (anyObj.originalStroke || 'green'));
        }
    });

    canvas.renderAll();
}

export function testCircuitLogic(canvas: fabric.Canvas, closedSignals: Set<string>) {
    // test circuit ke liye alag logic
}

export const circuitLogicRegistry: Record<string, (canvas: fabric.Canvas, closedSignals: Set<string>) => void> = {
    "mainCircuit": mainCircuitLogic,
    "testCircuit": testCircuitLogic,
    // yahan aur circuits add kar sakte ho
};

