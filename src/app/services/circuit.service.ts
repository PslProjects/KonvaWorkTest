import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  // private dataSendToBackend = '/api/save';
  private baseUrl = '/api/test';
  private getSavedCircuitsList = '/api/get';
  private ruleApi = '/api/rules';



  constructor(private http: HttpClient) { }

  // ⬇️ API to save circuit to backend
  saveCircuit(saveData: { objects: any[], redSignalLineMap: Record<string, string[]>, closedSignalsArray: string[] }): Observable<any> {
    return this.http.post('/api/save', saveData, { responseType: 'text' as 'json' });
  }


  
  testBackendConnection(): Observable<any> {
    return this.http.get(`${this.baseUrl}`, { responseType: 'text' });
  }

  // ✅ New GET API method
  getDataFromBackend(): Observable<any> {
    return this.http.get(this.getSavedCircuitsList);
  }
  // ====================================================
  // 🟦 LINE RULE APIs (DB-driven rules)
  // ====================================================
  getRulesByName(name: string): Observable<any[]> {
  return this.http.get<any[]>(`${this.ruleApi}/by-name/${encodeURIComponent(name)}`);
}
deleteRulesByName(name: string): Observable<any> {
  return this.http.delete(`${this.ruleApi}/by-name/${encodeURIComponent(name)}`);
}
  createRule(rule: any): Observable<any> {
    return this.http.post<any>(this.ruleApi, rule);
  }
  updateRule(id: number, rule: any): Observable<any> {
    return this.http.put<any>(`${this.ruleApi}/${id}`, rule);
  }
  deleteRuleApi(id: number): Observable<any> {
    return this.http.delete(`${this.ruleApi}/${id}`);
  }
}
